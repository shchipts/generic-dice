;   Copyright (c) 2021 International Institute for Applied Systems Analysis.
;   All rights reserved. The use and distribution terms for this software
;   are covered by the MIT License (http://opensource.org/licenses/MIT)
;   which can be found in the file LICENSE at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns ^{:doc "DICE updates from Hansel et al. (2020).

Hansel, M., Drupp, M., Johansson, D., Nesje, F., Azar, C., Freeman, M.,
Groom, B., & Sterner, T. (2020). Climate Economics Support for the UN Climate
Targets. Nature Climate Change, 10: 781-789"
      :author "Anna Shchiptsova"}
 generic-dice.instances.hansel2020
  (:require [clojure.math.numeric-tower :as math]))

(defn decarbonization
  "Hansel et al. (2020) feasible decarbonization.

Units:
  increase in emissions reductions - GtCO2 per time-step"
  [start-year time-step]
  {:pre-peak {:reduction {:growth (* time-step 2)}
              :reduction-rate {:maximum 1}}
   :net-zero-timing
   (reduce
    (fn [seed next]
      (if (< next 2045)
        (inc seed)
        (reduced seed)))
    0
    (iterate #(+ % time-step) start-year))
   :post-peak {:reduction-rate
               {:maximum 1.2
                :growth-rate (math/expt 1.1 (/ time-step 5))}}})
