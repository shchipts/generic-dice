;   Copyright (c) 2021 International Institute for Applied Systems Analysis.
;   All rights reserved. The use and distribution terms for this software
;   are covered by the MIT License (http://opensource.org/licenses/MIT)
;   which can be found in the file LICENSE at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns generic-dice.instances.dice2016-test
  (:require [clojure.test :refer :all]
            [generic-dice.instances.dice2016 :refer :all]
            [utilities-clj.floating-point-comparison :refer :all]))

;;; tests

(deftest labor
  (testing "labor input"
    (; Act
     let [dice1 (parameters 2015 5)
          dice2 (parameters 2020 5)
          dice3 (parameters 2015 10)
          dice4 (parameters 2050 5)]

      ; Assert
      (is (real= (:labor (:cobb-douglas dice1))
                 [7.403 7.853091 8.264921 8.638975 8.976557
                  9.279543 9.550180 9.790920 10.004299 10.192839
                  10.358983 10.505050 10.63320768 10.74545454 10.84361725
                  10.92935041 11.00414281 11.06932644]))
      (is (real= (:labor (:cobb-douglas dice2))
                 [7.853091 8.264921 8.638975 8.976557
                  9.279543 9.550180 9.790920 10.004299 10.192839
                  10.358983 10.505050 10.63320768 10.74545454 10.84361725
                  10.92935041 11.00414281 11.06932644]))
      (is (real= (:labor (:cobb-douglas dice3))
                 [7.403 8.264921 8.976557 9.550180 10.004299
                  10.358983 10.63320768 10.84361725 11.00414281]))
      (is (real= (:labor (:cobb-douglas dice4))
                 [9.790920 10.004299 10.192839 10.358983 10.505050
                  10.63320768 10.74545454 10.84361725 10.92935041 11.00414281
                  11.06932644])))))

(deftest tfp
  (testing "total factor productivity"
    (; Act
     let [dice1 (parameters 2015 5)
          dice2 (parameters 2020 5)
          dice3 (parameters 2015 10)
          dice4 (parameters 2050 5)]

      ; Assert
      (is (real= (:tfp (:cobb-douglas dice1))
                 [5.1150000 5.5357143 5.9788909 6.4448083 6.9336926
                  7.4457170 7.9810008 8.5396092 9.1215527 9.7267878
                  10.3552173 11.0066908 11.6810058 12.37790913 13.09709801
                  13.8382219 14.60088427 15.38464458]))
      (is (real= (:tfp (:cobb-douglas dice2))
                 [5.5357143 5.9788909 6.4448083 6.9336926
                  7.4457170 7.9810008 8.5396092 9.1215527 9.7267878
                  10.3552173 11.0066908 11.6810058 12.37790913 13.09709801
                  13.8382219 14.60088427 15.38464458]))
      (is (real= (:tfp (:cobb-douglas dice3))
                 [5.1150000 5.9788909 6.9336926 7.9810008 9.1215527
                  10.3552173 11.6810058 13.09709801 14.60088427]))
      (is (real= (:tfp (:cobb-douglas dice4))
                 [8.5396092 9.1215527 9.7267878 10.3552173 11.0066908
                  11.6810058 12.37790913 13.09709801 13.8382219 14.60088427
                  15.38464458])))))

(deftest carbon-intensity
  (testing "carbon intensity"
    (; Act
     let [dice1 (parameters 2015 5)
          dice2 (parameters 2020 5)
          dice3 (parameters 2015 10)
          dice4 (parameters 2050 5)]

      ; Assert
      (is (real= (:carbon-intensity dice1)
                 [0.35032003 0.32468228 0.30103494 0.27921523 0.25907432
                  0.24047608 0.22329595 0.20741990 0.19274355 0.17917124
                  0.16661534 0.15499552 0.1442381 0.1342755 0.1250456
                  0.1164916 0.1085611 0.1012061]))
      (is (real= (:carbon-intensity dice2)
                 [0.32468228 0.30103494 0.27921523 0.25907432
                  0.24047608 0.22329595 0.20741990 0.19274355 0.17917124
                  0.16661534 0.15499552 0.1442381 0.1342755 0.1250456
                  0.1164916 0.1085611 0.1012061]))
      (is (real= (:carbon-intensity dice3)
                 [0.35032003 0.30103494 0.25907432 0.22329595 0.19274355
                  0.16661534 0.1442381 0.1250456 0.1085611]))
      (is (real= (:carbon-intensity dice4)
                 [0.20741990 0.19274355 0.17917124 0.16661534 0.15499552
                  0.1442381 0.1342755 0.1250456 0.1164916 0.1085611
                  0.1012061])))))


;;; test grouping


(deftest parameters-test
  (testing "Some economic parameters in DICE 2016:\n"
    (labor)
    (tfp)
    (carbon-intensity)))


;;; tests in the namespace


(defn test-ns-hook
  "Explicit definition of tests in the namespace"
  []
  (parameters-test))
