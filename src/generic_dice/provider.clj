;   Copyright (c) 2021 International Institute for Applied Systems Analysis.
;   All rights reserved. The use and distribution terms for this software
;   are covered by the MIT License (http://opensource.org/licenses/MIT)
;   which can be found in the file LICENSE at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns ^{:doc "Forward and backward processing of I/O data."
      :author "Anna Shchiptsova"}
 generic-dice.provider
  (:require [utilities-clj.format :as formatter]
            [utilities-clj.writer :as writer]))

(defn- write-edn
  "Write clojure object to edn file"
  [data file options]
  (->> (pr-str data)
       vector
       (writer/txt-file (:save options) file)))

(defn write-tree
  "Write emissions tree to edn file"
  [graph options opts]
  (-> (hash-map :tree graph)
      (merge opts)
      (write-edn
       (str "tree "
            (:parameters options)
            ".edn")
       options)))

(defn write-tree-stats
  "Write levels information for emissions tree to csv file"
  [graph options]
  (->> ((juxt :level-size
              :gross
              :abated)
        graph)
       (#(conj
          %
          (let [{start-year :start
                 end-year :end
                 time-step :step} options]
            (range (+ start-year time-step)
                   (+ end-year time-step)
                   time-step))))
       (#(conj % [["Year" "Gross" "Abated" "Emitted"]]))
       (iterate
        (fn [[ls gross abated years out]]
          (list
           (rest ls)
           (drop (first ls) gross)
           (drop (first ls) abated)
           (rest years)
           (->> (vector (repeat (first years))
                        gross
                        abated)
                (map #(take (first ls) %))
                ((juxt vec
                       #(apply map - (rest %))))
                (apply conj)
                (apply map vector)
                (reduce
                 (fn [seed x]
                   (->> (:grid options)
                        ((fn [h]
                           (map #(formatter/double-to-str (* % h) 6)
                                (rest x))))
                        (#(conj % (str (first x))))
                        (conj seed)))
                 out)))))
       (drop-while (comp seq first))
       first
       last
       (writer/csv-file
        (:save options)
        (str "stats " (:parameters options) ".csv"))))

(defn write-tree-options
  "Write options for emissions tree construction to edn file"
  [init parameters constraints options opts]
  (write-edn
   (assoc opts
          :initial-values init
          :parameters parameters
          :constraints constraints)
   (str "options "
        (:parameters options)
        ".edn")
   options))

(defn write-emissions-paths
  "Write emissions paths to csv files"
  [paths gross abated h output-dir]
  (doall
   (map
    (fn [[values file]]
      (writer/csv-file
       output-dir
       file
       (map
        (fn [path]
          (map (fn [id]
                 (->> (dec id)
                      (nth values)
                      (* h)
                      (#(formatter/double-to-str % 6))))
               path))
        paths)))
    [[gross "gross emissions.csv"]
     [abated "abated emissions.csv"]
     [(vec (map - gross abated)) "emissions.csv"]])))
