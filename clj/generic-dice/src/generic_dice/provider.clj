;   Copyright (c) 2022 International Institute for Applied Systems Analysis.
;   All rights reserved. The use and distribution terms for this software
;   are covered by the MIT License (http://opensource.org/licenses/MIT)
;   which can be found in the file LICENSE at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns ^{:doc "Forward and backward processing of I/O data."
      :author "Anna Shchiptsova"}
 generic-dice.provider
  (:require [utilities-clj.reader :as reader]
            [utilities-clj.format :as formatter]
            [utilities-clj.writer :as writer]))

(defn write-net-emissions-ffi
  "Writes net FFI emissions into csv files per SSP scenario"
  [output-dir ts data]
  (->> (keys data)
       (map
        (fn [ssp]
          (->> (get data ssp)
               ((juxt :parameters :paths))
               (apply
                map
                (fn [pars path]
                  (->> (map #(formatter/double-to-str (double %1) %2)
                            pars
                            [0 2 0 2 0 0])
                       vec
                       (#(reduce
                          (fn [seed x]
                            (conj seed (formatter/double-to-str x 6)))
                          %
                          path)))))
               (#(->> (map str ts)
                      (concat (list "id" "y1" "x1" "K" "midpoint_offset" "dt"))
                      (conj %)))
               (writer/csv-file
                output-dir
                (str "net-emissions " (name ssp) ".csv")))))
       dorun))

(defn read-temperature-curves
  "Reads net FFI emissions parameters and temperature curves from csv file"
  [path]
  (let [to-int #(Integer/parseInt %)
        to-double #(Double/parseDouble %)]
    (->> (reader/read-csv path)
         rest
         (map
          (fn [row]
            ((juxt (fn [[pars _]]
                     (map
                      #(%2 %1)
                      pars
                      [to-int to-double to-int to-double to-int to-int]))
                   (fn [[_ curves]]
                     (map to-double curves)))
             (split-at 6 row))))
         (apply map list))))

(defn write-economic-output
  "Writes SSP economy pathways into csv file"
  [output-dir ssp ts data]
  (->> (keys data)
       (remove #{:net-emissions :cdr})
       (map
        (fn [output]
          (->> ((juxt :net-emissions
                      :cdr
                      #(get % output))
                data)
               (apply
                map
                (fn [pars1 pars2 path]
                  (->> (concat pars1 pars2)
                       ((fn [coll]
                          (map #(formatter/double-to-str (double %1) %2)
                               coll
                               [0 2 2 0 2 0 0 0 2 0 0])))
                       vec
                       (#(reduce
                          (fn [seed x]
                            (conj seed (formatter/double-to-str x 6)))
                          %
                          path)))))
               (#(->> (map str ts)
                      (concat
                       (list "id"
                             "y0"
                             "y1"
                             "x1"
                             "K"
                             "midpoint_offset"
                             "dt"
                             "id_CDR"
                             "K_CDR"
                             "midpoint_CDR"
                             "dt_CDR"))
                      (take (count (first %)))
                      (conj %)))
               (writer/csv-file
                output-dir
                (str (name output) " " (name ssp) ".csv")))))
       dorun))

(defn write-cdr-emissions
  "Writes CDR emissions into csv file"
  [output-dir ts data]
  (->> ((juxt :parameters :paths) data)
       (apply
        map
        (fn [pars path]
          (->> (map #(formatter/double-to-str (double %1) %2)
                    pars
                    [0 2 0 0])
               vec
               (#(reduce
                  (fn [seed x]
                    (conj seed (formatter/double-to-str x 6)))
                  %
                  path)))))
       (#(->> (map str ts)
              (concat (list "id_CDR" "K_CDR" "midpoint_CDR" "dt_CDR"))
              (conj %)))
       (writer/csv-file output-dir "cdr-emissions.csv")))
