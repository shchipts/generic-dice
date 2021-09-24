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

(def cummulative-emissions-volume
  "Remaining emissions quotas (GtCO2).

Taken from Friedlingstein et al. (2014) for 50% probability of global-mean
warming below 3 °C.

Friedlingstein, P., Andrew, R., Rogelj, J., Peters, G., Canadell, J.,
Knutti, R., Luderer, G., Raupach, M., Schaeffer, M., van Vuuren, D., &
Le Quere, C. (2014). Persistent Growth of CO2 Emissions and Implications for
Reaching Climate Targets. Nature Geoscience, Advanced Online Publication."
  {:cumulative-emissions {:maximum 3300}})
