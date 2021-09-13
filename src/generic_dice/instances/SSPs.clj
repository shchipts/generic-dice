;   Copyright (c) 2021 International Institute for Applied Systems Analysis.
;   All rights reserved. The use and distribution terms for this software
;   are covered by the MIT License (http://opensource.org/licenses/MIT)
;   which can be found in the file LICENSE at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns ^{:doc "SSP data"
      :author "Anna Shchiptsova"}
 generic-dice.instances.SSPs
  (:require [clojure.math.numeric-tower :as math]
            [generic-dice.instances.common :as common]
            [generic-dice.instances.dice2016 :as dice2016]))

(def ^:private SSPs
  "Data from Shared Socioeconomic Pathways.

Time series are downloaded from SSP Database Version 2.0. Additional values are
taken from Leimbach et al. (2017) following Glanemann et al. (2020).

1. [SSP Database] https://tntcat.iiasa.ac.at/SspDb/
2. Leimbach, M., Kriegler, E., Roming, N., & Schwanitz, J. (2017). Future
Growth Patterns of World Regions – A GDP Scenario Approach. Global Environmental
Change, 42: 215-225
3. Glanemann, N, Willner, S., & Levermann, A. (2020). Paris Climate Agreement
Passes The Cost-Benefit Test. Nature Communiations, 11(110): 1-11"
  {:population
   {:SSP1 [6921.798 7576.105 8061.938 8388.763 8530.500 8492.176 8298.950
           7967.387 7510.454 6957.989]
    :SSP2 [6867.390 7611.250 8261.990 8787.120 9169.110 9384.700 9456.880
           9407.260 9253.950 9032.420]
    :SSP3 [6879.590 7697.854 8514.307 9257.220 9957.131 10574.362 11117.377
           11633.415 12134.327 12620.136]
    :SSP4 [6895.882 7626.353 8258.565 8765.680 9146.527 9377.450 9472.529
           9471.440 9401.586 9292.446]
    :SSP5 [6894.000 7552.000 8054.000 8403.000 8579.000 8589.000 8457.000
           8200.000 7831.000 7375.000]}
   :industrial-emissions
   {:SSP1 [31981.484 36892.236 39680.275 41860.776 42668.610 43038.298
           40936.403 35332.481 30633.360 27049.004]
    :SSP2 [33133.194 37148.025 42507.302 47740.715 53614.471 59467.088
           65089.405 74067.978 81276.948 86165.768]
    :SSP3 [32637.746 41088.257 49158.915 55239.448 60206.474 64222.706
           68480.648 72728.153 77572.247 82558.844]
    :SSP4 [32646.989 40417.488 47694.335 51591.229 53078.259 53691.960
           52929.922 49420.810 46575.217 44329.322]
    :SSP5 [32200.000 39550.000 51160.000 65840.000 82130.000 101200.000
           117700.000 129600.000 130900.000 127600.000]}
   :gdp
   {:SSP1 [68461.8828125 101815.2968750 155854.7968750 223195.5000000
           291301.4062500 356291.4062500 419291.1875000 475419.1875000
           524875.8125000 565389.6250000]
    :SSP2 [67528.8 101244.5 143069.7 185954.6 231300.2 280515.4 336848.5
           398498.2 465846.5 539332.4]
    :SSP3 [64812.7191 97447.9969 130402.0000 154577.0000 173654.0000
           190084.0000 207896.0000 226734.0000 247081.0000 270265.0000]
    :SSP4 [66937.0234282228 99822.5580727285 141254.1120976990 182424.1463024310
           219113.7769355710 251626.1639388320 281788.3842443670
           307379.0681369890 330347.6151104890 352091.4619067460]
    :SSP5 [67570 101900 165800 260200 364700 478300 605800 740600 882300
           1031000]}
   :capital-elasticity
   {:SSP1 0.35 :SSP2 0.35 :SSP3 0.25 :SSP4 0.3 :SSP5 0.45}})

(defn- interpolate
  "Linearly interpolates SSP time series"
  [ssp var]
  (let [interval 10
        coll ((comp ssp var) SSPs)]
    (->> (first coll)
         vector
         (vector coll)
         (iterate
          (fn [[new-coll seed]]
            ((juxt rest
                   (fn [x]
                     (let [y0 (first x)
                           y1 (second x)]
                       (-> (- y1 y0)
                           (/ interval)
                           (* (/ interval 2))
                           (+ y0)
                           (vector y1)
                           (#(into seed %))))))
             new-coll)))
         (drop-while (comp seq rest first))
         first
         last
         rest)))

(defn- get-gdp
  "GDP series based on SSP projection (trillion 2010 USD)"
  [ssp]
  (map #(/ (* % 1.134789528) 1000) (interpolate ssp :gdp)))

(defn- get-labor
  "Population series based on SSP projection (billion)"
  [ssp]
  (map #(/ % 1000) (interpolate ssp :population)))

(defn- get-industrial-emissions
  "Industrial emissions series based on SSP projection (GtCO2)"
  [ssp]
  (map #(/ % 1000) (interpolate ssp :industrial-emissions)))

(defn- get-carbon-intensity
  "Carbon intensity series calculated from GDP and emissions SSP projections
(tCO2/thousands 2010 USD)"
  [ssp]
  (->> (get-industrial-emissions ssp)
       (#(vector % (get-gdp ssp)))
       (apply map #(/ %1 %2))))

(defn- get-tfp
  "Total factor productivity series calculated from GDP and population SSP
projections based on Leimbach et al. (2017) (unitless)"
  [ssp]
  (let [alpha (get (:capital-elasticity SSPs) ssp)
        p_k 0.12]
    (map
     (fn [gdp labor]
       (-> (* alpha gdp)
           (/ p_k)
           (math/expt alpha)
           (* (math/expt labor (- 1 alpha)))
           (#(/ gdp %))))
     (get-gdp ssp)
     (get-labor ssp))))

(defn volume
  "SSP-based constraints on volume of produced industrial emissions (GtCO2)"
  [ssp start-year time-step]
  (-> (fn [_] (get-industrial-emissions ssp))
      (common/take-by start-year time-step)
      (#(hash-map :industrial-emissions {:produced {:maximum %}}))))

(defn parameters
  "SSP-based parameters.

Units:
  labor input L(t) - billions
    (from start-year to 2100 by time-step)
  total factor productivity A(t) - unitless
    (from start-year to 2100 by time-step)
  capital elasticity \\alpha - unitless

  carbon intensity \\sigma(t) - tCO2/thousands 2010 USD
    (from start-year to 2100 by time-step)"
  [ssp start-year time-step]
  (->> (vector get-carbon-intensity get-labor get-tfp)
       (map #(common/take-by (fn [_] (% ssp)) start-year time-step))
       ((fn [[carbon-intensity labor tfp]]
          {:carbon-intensity carbon-intensity
           :cobb-douglas {:labor labor
                          :tfp tfp
                          :capital-elasticity
                          (get (:capital-elasticity SSPs) ssp)}}))
       (merge (select-keys (dice2016/parameters start-year time-step)
                           [:depreciation-rate]))))
