;   Copyright (c) 2021 International Institute for Applied Systems Analysis.
;   All rights reserved. The use and distribution terms for this software
;   are covered by the MIT License (http://opensource.org/licenses/MIT)
;   which can be found in the file LICENSE at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns ^{:doc "Some common functions"
      :author "Anna Shchiptsova"}
 generic-dice.instances.common
  (:require [clojure.math.numeric-tower :as math]))

(defn take-by
  "Filters generated collection (from 2015 to 2100 by 5 year period) by start
year and specified time period (which must be divided by 5 without remainder)"
  [generatef start-year time-step]
  (let [stepf #(int (/ % 5))
        numf #(stepf (- % 2015))]
    (->> (numf 2100)
         inc
         generatef
         (drop (numf start-year))
         (vector [])
         (iterate
          (fn [[seed coll]]
            ((juxt #(conj seed (first %))
                   #(drop (stepf time-step) %))
             coll)))
         (drop-while (comp seq last))
         first
         first)))
