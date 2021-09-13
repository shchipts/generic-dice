;   Copyright (c) 2021 International Institute for Applied Systems Analysis.
;   All rights reserved. The use and distribution terms for this software
;   are covered by the MIT License (http://opensource.org/licenses/MIT)
;   which can be found in the file LICENSE at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns generic-dice.instances.hansel2020-test
  (:require [clojure.test :refer :all]
            [generic-dice.instances.hansel2020 :refer :all]
            [utilities-clj.floating-point-comparison :refer :all]))

;;; tests

(deftest net-zero-timing
  (testing "net-zero timing"
    (; Act
     let [hansel1 (decarbonization 2015 5)
          hansel2 (decarbonization 2020 5)
          hansel3 (decarbonization 2045 5)
          hansel4 (decarbonization 2050 5)
          hansel5 (decarbonization 2055 5)

          hansel6 (decarbonization 2015 10)
          hansel7 (decarbonization 2025 10)
          hansel8 (decarbonization 2045 10)
          hansel9 (decarbonization 2055 10)
          hansel10 (decarbonization 2065 10)

          hansel11 (decarbonization 2020 10)
          hansel12 (decarbonization 2030 10)
          hansel13 (decarbonization 2040 10)
          hansel14 (decarbonization 2050 10)
          hansel15 (decarbonization 2060 10)]

      ; Assert
      (is (= (:net-zero-timing hansel1)
             7))
      (is (= (:net-zero-timing hansel2)
             6))
      (is (= (:net-zero-timing hansel3)
             1))
      (is (= (:net-zero-timing hansel4)
             0))
      (is (= (:net-zero-timing hansel5)
             0))

      (is (= (:net-zero-timing hansel6)
             4))
      (is (= (:net-zero-timing hansel7)
             3))
      (is (= (:net-zero-timing hansel8)
             1))
      (is (= (:net-zero-timing hansel9)
             0))
      (is (= (:net-zero-timing hansel10)
             0))

      (is (= (:net-zero-timing hansel11)
             3))
      (is (= (:net-zero-timing hansel12)
             2))
      (is (= (:net-zero-timing hansel13)
             1))
      (is (= (:net-zero-timing hansel14)
             0))
      (is (= (:net-zero-timing hansel15)
             0)))))

(deftest reduction-jumps
  (testing "changes in emissions reductions pre- and post-peak warming"
    (; Act
     let [hansel1 (decarbonization 2015 5)
          hansel2 (decarbonization 2015 10)]

      ; Assert
      (is (real= (:growth (:reduction (:pre-peak hansel1)))
                 10))
      (is (real= (:growth (:reduction (:pre-peak hansel2)))
                 20))

      (is (real= (:growth-rate (:reduction-rate (:post-peak hansel1)))
                 1.1))
      (is (real= (:growth-rate (:reduction-rate (:post-peak hansel2)))
                 1.21)))))


;;; test grouping


(deftest decarbonization-test
  (testing "Decarbanization in Hansel et al. (2020):\n"
    (net-zero-timing)
    (reduction-jumps)))


;;; tests in the namespace


(defn test-ns-hook
  "Explicit definition of tests in the namespace"
  []
  (decarbonization-test))
