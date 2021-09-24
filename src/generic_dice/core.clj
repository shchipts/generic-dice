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
  (:require [clojure.java.io :as io]
            [dice-simulator.procedures :as proc]
            [generic-dice.instances.dice2016 :as dice2016]
            [generic-dice.instances.hansel2020 :as hansel2020]
            [generic-dice.instances.SSPs :as ssp]
            [generic-dice.provider :as provider]
            [utilities-clj.cmd :as cmd]
            [utilities-clj.reader :as reader])
  (:gen-class))

(def ^{:private true} labels
  "Labels of supported arguments"
  {:parameters ["DICE2016" "SSP1" "SSP2" "SSP3" "SSP4" "SSP5"]
   :initial-values ["DICE2016"]
   :decarbonization ["Hansel2020"]
   :volume ["SSP1" "SSP2" "SSP3" "SSP4" "SSP5"]
   :commands [:emissions-tree :emissions-paths]})

(def ^:private cli-options
  "Command line options."
  [["-c" "--command NAME" "What to simulate from the model?"
    :id :command
    :default :emissions-tree
    :parse-fn keyword
    :validate
    [#(some (fn [label] (= % label)) (:commands labels))
     (fn [x]
       (->> (:commands labels)
            (map (fn [l] (str " " l)))
            (apply
             str
             x
             " not supported simulation command\n(supported")
            (#(str % ")"))))]]
   ["-g" "--grid-step G" "Emissions grid step G (GtC)"
    :id :grid
    :default 0.5
    :parse-fn #(Double/parseDouble %)
    :validate
    [pos?
     (fn [_] (str "The value of grid step should be positive"))]]
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
   ["-p" "--parameters MODEL" "MODEL instance: parameters"
    :id :parameters
    :default "DICE2016"
    :validate
    [#(some (fn [label] (= % label)) (:parameters labels))
     (fn [x]
       (->> (:parameters labels)
            (map (fn [l] (str " " l)))
            (apply
             str
             x
             " not in supported collections of parameters\n(supported")
            (#(str % ")"))))]]
   ["-i" "--initial-values MODEL" "MODEL instance: initial values"
    :id :init
    :default "DICE2016"
    :validate
    [#(some (fn [label] (= % label)) (:initial-values labels))
     (fn [x]
       (->> (:initial-values labels)
            (map (fn [l] (str " " l)))
            (apply
             str
             x
             " not in supported collections of initial values\n(supported")
            (#(str % ")"))))]]
   ["-d" "--decarbonization MODEL" "MODEL instance: decarbonization constraints"
    :id :decarbonization
    :default "Hansel2020"
    :validate
    [#(some (fn [label] (= % label)) (:decarbonization labels))
     (fn [x]
       (->> (:decarbonization labels)
            (map (fn [l] (str " " l)))
            (apply
             str
             x
             " not in supported collections of decarbonization constraints\n"
             "(supported")
            (#(str % ")"))))]]
   ["-v" "--volume MODEL" "MODEL instance: constraints on emissions volume"
    :id :volume
    :default "SSP2"
    :validate
    [#(some (fn [label] (= % label)) (:volume labels))
     (fn [x]
       (->> (:volume labels)
            (map (fn [l] (str " " l)))
            (apply
             str
             x
             " not in supported collections of emissions volume constraints\n"
             "(supported")
            (#(str % ")"))))]]
   ["-f" "--input FILE" "Path to file with simulation input data"
    :id :input]
   ["-r" "--folder PATH" "PATH to folder for output writing"
    :id :save
    :default "bin"]
   ["-o" "--options" "Print options to file"
    :id :oprint
    :default false]])

(defn- initial-values
  "Initial values:
    industrial CO2 emissions E_ind(1) - GtCO2
    emissions reduction rate - unitless"
  [{init :init}]
  (condp = init
    "DICE2016" dice2016/initial-values))

(defn- parameters
  "Selected parameters for economic module:
    labor input L(t) - billions
    total factor productivity A(t) - unitless
    capital elasticity \\alpha - unitless
    carbon intensity \\sigma(t) - tCO2/thousands 2010 USD"
  [{pars :parameters start-year :start time-step :step}]
  ((condp = pars
     "DICE2016" dice2016/parameters
     "SSP1" (partial ssp/parameters :SSP1)
     "SSP2" (partial ssp/parameters :SSP2)
     "SSP3" (partial ssp/parameters :SSP3)
     "SSP4" (partial ssp/parameters :SSP4)
     "SSP5" (partial ssp/parameters :SSP5))
   start-year time-step))

(defn- decarbonization
  "Constraints on feasible decarbonization:
increase in emissions reductions - GtCO2 per time-step"
  [{constraints :decarbonization start-year :start time-step :step}]
  ((condp = constraints
     "Hansel2020" hansel2020/decarbonization)
   start-year time-step))

(defn- volume
  "Constraints on volume of produced industrial emissions (GtCO2)"
  [{constraints :volume start-year :start time-step :step}]
  ((condp = constraints
     "SSP1" (partial ssp/volume :SSP1)
     "SSP2" (partial ssp/volume :SSP2)
     "SSP3" (partial ssp/volume :SSP3)
     "SSP4" (partial ssp/volume :SSP4)
     "SSP5" (partial ssp/volume :SSP5))
   start-year time-step))

(defn- tree
  "A sampled graph of temporal emissions"
  [options]
  (-> (parameters options)
      (assoc :time-step (:step options))
      (vector (initial-values options))
      (conj {:volume (volume options)
             :decarbonization (decarbonization options)})
      (conj {:options (dissoc options :trace :save :oprint)})
      ((juxt (fn [[pars init constraints opts]]
               (-> (:grid options)
                   (proc/emissions-tree init pars constraints)
                   (#(drop (-> (:end options)
                               (- (:start options))
                               (/ (:step options))
                               dec)
                           %))
                   first
                   ((juxt #(provider/write-tree % options opts)
                          #(provider/write-tree-stats % options)))))
             (fn [[pars init constraints opts]]
               (if (:oprint options)
                 (provider/write-tree-options
                  init
                  pars
                  constraints
                  options
                  opts)))))))

(defn- paths
  "Generated emissions pathways"
  [{input-file :input output-dir :save}]
  (->> (reader/load-edn input-file)
       ((juxt (fn [{tree :tree options :options}]
                (proc/emissions-paths
                 tree
                 (:grid options)
                 (initial-values options)
                 (hash-map :time-step (:step options))
                 (volume options)))
              (comp :gross :tree)
              (comp :abated :tree)
              (comp :grid :options)))
       ((fn [[paths gross abated h]]
          (provider/write-emissions-paths paths gross abated h output-dir)))))

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
         :emissions-tree (tree options)
         :emissions-paths (paths options))))}))
