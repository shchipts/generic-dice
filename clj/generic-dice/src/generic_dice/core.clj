;   Copyright (c) 2021 International Institute for Applied Systems Analysis.
;   All rights reserved. The use and distribution terms for this software
;   are covered by the MIT License (http://opensource.org/licenses/MIT)
;   which can be found in the file LICENSE at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns ^{:doc "Command line and I/O operations"
      :author "Anna Shchiptsova"}
 generic-dice.core
  (:require [dice-simulator.commands :as commands]
            [generic-dice.instances.dice2016 :as dice2016]
            [generic-dice.provider :as provider]
            [utilities-clj.cmd :as cmd])
  (:gen-class))

(def ^{:private true} labels
  "Labels of supported arguments"
  {:scenarios [:SSP1 :SSP2 :SSP3 :SSP4 :SSP5]
   :initial-values ["DICE2016"]
   :damages [:howard-sterner2017]
   :costs [:dice2016 :dice2013]
   :commands [:net-emissions-ffi :economy :cdr]})

(def ^:private cli-options
  "Command line options."
  [["-c" "--command NAME" "What to simulate from the model?"
    :id :command
    :default :net-emissions-ffi
    :parse-fn keyword
    :validate
    [#(some (fn [label] (= % label)) (:commands labels))
     (fn [x]
       (->> (:commands labels)
            (map (fn [l] (str " " (name l))))
            (apply
             str
             (name x)
             " not supported simulation command\n(supported")
            (#(str % ")"))))]]
   ["-z" "--time-step Z" "Time step Z"
    :id :step
    :default 5
    :parse-fn #(Integer/parseInt %)
    :validate
    [#(zero? (rem % 5))
     #(str "Time step value " % " is not supported. Should be 5, 10, 15,..")]]
   ["-s" "--start-year Y" "Start year Y"
    :id :start
    :default 2015
    :parse-fn #(Integer/parseInt %)
    :validate
    [#(and (zero? (rem % 5)) (>= % 2015) (< % 2100))
     (fn [_]
       (str "Start year should take values 2015, 2020, 2025, ..., 2095"))]]
   ["-e" "--end-year Y" "End year Y"
    :id :end
    :default 2100
    :parse-fn #(Integer/parseInt %)
    :validate
    [#(and (zero? (rem % 5)) (> % 2015) (<= % 2100))
     (fn [_]
       (str "End year should take values 2020, 2025, 2030, ..., 2100"))]]
   ["-i" "--initial-values MODEL" "MODEL instance: initial values"
    :id :init
    :default "DICE2016"
    :validate
    [#(some (fn [label] (= % label)) (:initial-values labels))
     (fn [x]
       (->> (:initial-values labels)
            (map (fn [l] (str " " (name l))))
            (apply
             str
             (name x)
             " not in supported collections of initial values\n(supported")
            (#(str % ")"))))]]
   ["-d" "--damages MODEL" "MODEL instance: damage function"
    :id :damages
    :default :howard-sterner2017
    :parse-fn keyword
    :validate
    [#(some (fn [label] (= % label)) (:damages labels))
     (fn [x]
       (->> (:damages labels)
            (map (fn [l] (str " " (name l))))
            (apply
             str
             (name x)
             " not supported damage function\n(supported")
            (#(str % ")"))))]]
   ["-a" "--costs MODEL" "MODEL instance: abatement cost function"
    :id :costs
    :default :dice2016
    :parse-fn keyword
    :validate
    [#(some (fn [label] (= % label)) (:costs labels))
     (fn [x]
       (->> (:costs labels)
            (map (fn [l] (str " " (name l))))
            (apply
             str
             (name x)
             " not supported cost function\n(supported")
            (#(str % ")"))))]]
   ["-p" "--scenario MODEL" "MODEL instance: SSP scenario"
    :id :scenario
    :default :SSP1
    :parse-fn keyword
    :validate
    [#(some (fn [label] (= % label)) (:scenarios labels))
     (fn [x]
       (->> (:scenarios labels)
            (map (fn [l] (str " " (name l))))
            (apply
             str
             (name x)
             " not supported SSP scenario\n(supported")
            (#(str % ")"))))]]
   ["-f" "--input FILE" "Path to FILE with simulation input data"
    :id :input]
   ["-r" "--folder PATH" "PATH to folder for output writing"
    :id :save
    :default "bin"]
   ["-o" "--options" "Print options to file"
    :id :oprint
    :default false]])

(def ^:private cdr-pars
  "Parameterized CDR curves"
  (map
   (fn [id K]
     (vector (inc id) K 2050 40))
   (range)
   (range 0 20.5 0.5)))

(defn- initial-values
  "Initial values:
    industrial CO2 emissions E_ind(1) - GtCO2
    emissions reduction rate - unitless"
  [{init :init}]
  (condp = init
    "DICE2016" dice2016/initial-values))

(defn- net-emissions-ffi-pars
  "Defines parameters for net FFI emissions curves"
  [options]
  (hash-map
   :y0
   (:industrial-emissions (initial-values options))
   :y_s
   (range 36 65.5 0.5)
   :x1s
   (range 2020 2065 5)
   :logistic-pars
   (->> (iterate
         (fn [coll]
           (->> (first coll)
                ((juxt #(+ (first %) 0.5)
                       #(inc (second %))
                       last))
                (conj coll)))
         (list [-20 15 50]))
        (drop-while #(neg? (ffirst %)))
        first)))

(defn- net-emissions-ffi
  "Generates net FFI emissions pathways"
  [options]
  (->> ((juxt :start
              #(+ (:end %) (:step %))
              :step)
        options)
       (apply range)
       ((juxt identity
              (fn [ts]
                (->> (net-emissions-ffi-pars options)
                     ((juxt :y0 :y_s :x1s :logistic-pars))
                     (#(conj % ts))
                     (apply commands/net-emissions-ffi)))))
       (apply provider/write-net-emissions-ffi (:save options))))

(defn- economic-growth
  "Generates economic output"
  [options]
  (->> ((juxt :start
              #(+ (:end %) (:step %))
              :step)
        options)
       (apply range)
       ((juxt identity
              (fn [ts]
                (->> (:input options)
                     provider/read-temperature-curves
                     ((fn [[net-emissions-pars temperature-curves]]
                        (-> (initial-values options)
                            :industrial-emissions
                            (#(map
                               (fn [pars]
                                 (-> (rest pars)
                                     (conj %)
                                     (conj (first pars))))
                               net-emissions-pars))
                            (commands/economic-growth
                             cdr-pars
                             temperature-curves
                             (:damages options)
                             (:costs options)
                             (:scenario options)
                             ts))))))))
       (apply
        provider/write-economic-output
        (str (:save options)
             "/damages "
             (name (:damages options))
             " costs "
             (name (:costs options)))
        (:scenario options))))

(defn- cdr-emissions
  "Generates CDR emissions pathways"
  [options]
  (->> ((juxt :start
              #(+ (:end %) (:step %))
              :step)
        options)
       (apply range)
       ((juxt identity
              (partial commands/cdr-emissions cdr-pars)))
       (apply provider/write-cdr-emissions (:save options))))

(defn -main
  "Performs simulations from generic DICE model"
  [& args]
  (cmd/terminal
   {:short-desc "Generic DICE as simulation model"
    :args args
    :options cli-options
    :execute
    (fn [_ options]
      (time
       (condp = (:command options)
         :net-emissions-ffi (net-emissions-ffi options)
         :economy (economic-growth options)
         :cdr (cdr-emissions options))))}))
