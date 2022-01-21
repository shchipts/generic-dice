;   Copyright (c) 2021 International Institute for Applied Systems Analysis.
;   All rights reserved. The use and distribution terms for this software
;   are covered by the MIT License (http://opensource.org/licenses/MIT)
;   which can be found in the file LICENSE at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns ^{:doc "DICE 2016 (Nordhaus 2017).

Based on DICE-2016R2-083017.gms available at
  https://sites.google.com/site/williamdnordhaus/dice-rice
Nordhaus, W. (2017). Revisiting the Social Cost of Carbon.
  PNAS, 114(7): 1518-1523"
      :author "Anna Shchiptsova"}
 generic-dice.instances.dice2016
  (:require [clojure.math.numeric-tower :as math]))

(defn- take-by
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

(def initial-values
  "DICE 2016 initial values in year 2015.

Units:
  industrial CO2 emissions E_ind(1) - GtCO2 per year
  emissions reduction rate - unitless"
  {:industrial-emissions 35.85
   :reduction-rate 0.03})

(defn- get-labor
  "Labor input in 2015-2020 and in n-1 subsequent 5-year periods (billions)."
  [n]
  (take n (iterate #(* % (math/expt (/ 11.5 %) 0.134)) 7.403)))

(defn- get-tfp
  "Total factor productivity in 2015-2020 and in n-1 subsequent 5-year periods
(unitless)."
  [n]
  (let [time-step 5]
    (reduce
     (fn [seed i]
       (->> (* time-step i)
            (* 0.005)
            -
            (math/expt Math/E)
            (* 0.076)
            (- 1)
            (/ (last seed))
            (conj seed)))
     [5.115]
     (range (dec n)))))

(defn- get-carbon-intensity
  "Carbon intensity in 2015-2020 and in n-1 subsequent 5-year periods
(tCO2/thousands 2010 USD)"
  [n]
  (let [time-step 5
        e0 (:industrial-emissions initial-values)]
    (->> (/ e0 (* 105.5 (- 1 0.03)))
         (vector -0.0152)
         (iterate (fn [[g s]]
                    (vector (* g (math/expt (inc -0.001) time-step))
                            (* s (math/expt Math/E (* g time-step))))))
         (map second)
         (take n))))

(defn parameters
  "DICE 2016 selected parameters.

Units:
  labor input L(t) - billions
    (from start-year to 2105 by time-step)
  total factor productivity A(t) - unitless
    (from start-year to 2105 by time-step)
  capital elasticity \\alpha - unitless

  carbon intensity \\sigma(t) - tCO2/thousands 2010 USD
    (from start-year to 2105 by time-step)"
  [start-year time-step]
  (->> (vector get-carbon-intensity get-labor get-tfp)
       (map #(take-by % start-year time-step))
       ((fn [[carbon-intensity labor tfp]]
          {:depreciation-rate 0.1
           :carbon-intensity carbon-intensity
           :cobb-douglas {:labor labor
                          :tfp tfp
                          :capital-elasticity 0.3}}))))
