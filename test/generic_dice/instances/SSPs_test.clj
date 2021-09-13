;   Copyright (c) 2021 International Institute for Applied Systems Analysis.
;   All rights reserved. The use and distribution terms for this software
;   are covered by the MIT License (http://opensource.org/licenses/MIT)
;   which can be found in the file LICENSE at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns generic-dice.instances.SSPs-test
  (:require [clojure.test :refer :all]
            [generic-dice.instances.SSPs :refer :all]
            [utilities-clj.floating-point-comparison :refer :all]))

;;; tests

(deftest labor
  (testing "labor input"
    (; Act
     let [ssp1 (parameters :SSP1 2015 5)
          ssp2 (parameters :SSP2 2015 5)
          ssp3 (parameters :SSP3 2015 5)
          ssp4 (parameters :SSP4 2015 5)
          ssp5 (parameters :SSP5 2015 5)

          out1 (parameters :SSP1 2020 5)
          out2 (parameters :SSP1 2020 10)
          out3 (parameters :SSP1 2050 5)]

      ; Assert
      (is (real= (:labor (:cobb-douglas ssp1))
                 [7.248951 7.576105 7.819021 8.061938 8.225350
                  8.388763 8.459631 8.530500 8.511338 8.492176
                  8.395563 8.298950 8.133169 7.967387 7.738921
                  7.510454 7.234221 6.957989]))
      (is (real= (:labor (:cobb-douglas ssp2))
                 [7.239320 7.611250 7.936620 8.261990 8.524555
                  8.787120 8.978115 9.169110 9.276905 9.384700
                  9.420790 9.456880 9.432070 9.407260 9.330605
                  9.253950 9.143185 9.032420]))
      (is (real= (:labor (:cobb-douglas ssp3))
                 [7.288722 7.697854 8.106081 8.514307 8.885764
                  9.257220 9.607176 9.957131 10.265746 10.574362
                  10.845869 11.117377 11.375396 11.633415 11.883871
                  12.134327 12.377231 12.620136]))
      (is (real= (:labor (:cobb-douglas ssp4))
                 [7.261118 7.626353 7.942459 8.258565 8.512123
                  8.765680 8.956104 9.146527 9.261989 9.377450
                  9.424990 9.472529 9.471985 9.471440 9.436513
                  9.401586 9.347016 9.292446]))
      (is (real= (:labor (:cobb-douglas ssp5))
                 [7.223000 7.552000 7.803000 8.054000 8.228500
                  8.403000 8.491000 8.579000 8.584000 8.589000
                  8.523000 8.457000 8.328500 8.200000 8.015500
                  7.831000 7.603000 7.375000]))

      (is (real= (:labor (:cobb-douglas out1))
                 [7.576105 7.819021 8.061938 8.225350
                  8.388763 8.459631 8.530500 8.511338 8.492176
                  8.395563 8.298950 8.133169 7.967387 7.738921
                  7.510454 7.234221 6.957989]))
      (is (real= (:labor (:cobb-douglas out2))
                 [7.576105 8.061938 8.388763 8.530500 8.492176
                  8.298950 7.967387 7.510454 6.957989]))
      (is (real= (:labor (:cobb-douglas out3))
                 [8.530500 8.511338 8.492176 8.395563 8.298950
                  8.133169 7.967387 7.738921 7.510454 7.234221
                  6.957989])))))

(deftest tfp
  (testing "total factor productivity"
    (; Act
     let [ssp1 (parameters :SSP1 2015 5)
          ssp2 (parameters :SSP2 2015 5)
          ssp3 (parameters :SSP3 2015 5)
          ssp4 (parameters :SSP4 2015 5)
          ssp5 (parameters :SSP5 2015 5)

          out1 (parameters :SSP1 2020 5)
          out2 (parameters :SSP1 2020 10)
          out3 (parameters :SSP1 2050 5)]

      ; Assert
      (is (real= (:tfp (:cobb-douglas ssp1))
                 [3.701586474 4.040388807 4.61272567 5.11760835 5.736112555
                  6.298345341 6.8696632 7.407537166 7.94626059 8.468289958
                  9.014485201 9.555529395 10.09810382 10.64704491 11.21388476
                  11.7988129 12.39100041 13.01356395]))
      (is (real= (:tfp (:cobb-douglas ssp2))
                 [3.683485462 4.013558732 4.412844365 4.764151122 5.111832889
                  5.427487324 5.767738957 6.083959504 6.448026999 6.793337363
                  7.211242845 7.613440515 8.073101582 8.521394451 9.030675577
                  9.533004006 10.09392068 10.65169273]))
      (is (real= (:tfp (:cobb-douglas ssp3))
                 [5.576937979 6.141850457 6.642826111 7.085132843 7.333526641
                  7.559533724 7.689749735 7.810156444 7.902652967 7.989421673
                  8.112822346 8.229617752 8.362558843 8.488947781 8.634008173
                  8.77232152 8.945250252 9.110477518]))
      (is (real= (:tfp (:cobb-douglas ssp4))
                 [4.582461832 5.02223436 5.570290767 6.056563525 6.522148585
                  6.948105724 7.319188787 7.667400448 7.990822645 8.300962438
                  8.615619506 8.922339025 9.204414369 9.482863844 9.754698627
                  10.02530878 10.29701522 10.5688208]))
      (is (real= (:tfp (:cobb-douglas ssp5))
                 [2.29102182 2.474292659 2.823485605 3.121436114 3.540558435
                  3.9072344 4.296267198 4.651187611 5.034990827 5.395803595
                  5.804771494 6.197285034 6.6230324 7.039839787 7.495860326
                  7.950214304 8.448153614 8.951844045]))

      (is (real= (:tfp (:cobb-douglas out1))
                 [4.040388807 4.61272567 5.11760835 5.736112555 6.298345341
                  6.8696632 7.407537166 7.94626059 8.468289958 9.014485201
                  9.555529395 10.09810382 10.64704491 11.21388476 11.7988129
                  12.39100041 13.01356395]))
      (is (real= (:tfp (:cobb-douglas out2))
                 [4.040388807 5.11760835 6.298345341 7.407537166 8.468289958
                  9.555529395 10.64704491 11.7988129 13.01356395]))
      (is (real= (:tfp (:cobb-douglas out3))
                 [7.407537166 7.94626059 8.468289958 9.014485201 9.555529395
                  10.09810382 10.64704491 11.21388476 11.7988129 12.39100041
                  13.01356395])))))

(deftest carbon-intensity
  (testing "carbon intensity"
    (; Act
     let [ssp1 (parameters :SSP1 2015 5)
          ssp2 (parameters :SSP2 2015 5)
          ssp3 (parameters :SSP3 2015 5)
          ssp4 (parameters :SSP4 2015 5)
          ssp5 (parameters :SSP5 2015 5)

          out1 (parameters :SSP1 2020 5)
          out2 (parameters :SSP1 2020 10)
          out3 (parameters :SSP1 2050 5)]

      ; Assert
      (is (real= (:carbon-intensity ssp1)
                 [0.35643617 0.31930567 0.26187471 0.22435677 0.18956761
                  0.16527475 0.14478035 0.12907751 0.11662683 0.10644724
                  0.09541246 0.08603569 0.07511897 0.06549107 0.05811332
                  0.05143074 0.04662249 0.04215879]))
      (is (real= (:carbon-intensity ssp2)
                 [0.36696127 0.32333221 0.28731004 0.26181864 0.24170987
                  0.22623858 0.21405694 0.20426347 0.19469865 0.18681195
                  0.17779102 0.17027871 0.16676265 0.16379054 0.15837797
                  0.15374791 0.14679375 0.14078712]))
      (is (real= (:carbon-intensity ssp3)
                 [0.40039808 0.37156045 0.34903522 0.33220236 0.32282378
                  0.31491195 0.30994432 0.30552242 0.30145205 0.29773351
                  0.29383622 0.29027284 0.28630356 0.28266406 0.27953488
                  0.27666339 0.27275910 0.26918973]))
      (is (real= (:carbon-intensity ssp4)
                 [0.38610032 0.35680038 0.32207995 0.29754344 0.27030698
                  0.24921733 0.22970911 0.21346745 0.19987283 0.18803476
                  0.17614332 0.16552472 0.15308650 0.14168382 0.13264881
                  0.12424199 0.11738331 0.11094820]))
      (is (real= (:carbon-intensity ssp5)
                 [0.37309013 0.34202432 0.29860115 0.27191345 0.24202540
                  0.22298067 0.20866414 0.19844983 0.19164198 0.18645104
                  0.17793488 0.17121108 0.16185820 0.15420767 0.14144925
                  0.13073987 0.11905898 0.10906281]))

      (is (real= (:carbon-intensity out1)
                 [0.31930567 0.26187471 0.22435677 0.18956761 0.16527475
                  0.14478035 0.12907751 0.11662683 0.10644724 0.09541246
                  0.08603569 0.07511897 0.06549107 0.05811332 0.05143074
                  0.04662249 0.04215879]))
      (is (real= (:carbon-intensity out2)
                 [0.31930567 0.22435677 0.16527475 0.12907751 0.10644724
                  0.08603569 0.06549107 0.05143074 0.04215879]))
      (is (real= (:carbon-intensity out3)
                 [0.12907751 0.11662683 0.10644724 0.09541246 0.08603569
                  0.07511897 0.06549107 0.05811332 0.05143074 0.04662249
                  0.04215879])))))

(deftest other-parameters
  (testing "other economic parameters"
    (; Act
     let [ssp1 (parameters :SSP1 2015 5)
          ssp2 (parameters :SSP2 2015 5)
          ssp3 (parameters :SSP3 2015 5)
          ssp4 (parameters :SSP4 2015 5)
          ssp5 (parameters :SSP5 2015 5)]

      ; Assert
      (is (= (:depreciation-rate ssp1)
             (:depreciation-rate ssp2)
             (:depreciation-rate ssp3)
             (:depreciation-rate ssp4)
             (:depreciation-rate ssp5)
             0.1)))))

(deftest volume-test
  (testing "baseline emissions with no climate change and no abatement"
    (; Act
     let [ssp1 (volume :SSP1 2015 5)
          ssp2 (volume :SSP2 2015 5)
          ssp3 (volume :SSP3 2015 5)
          ssp4 (volume :SSP4 2015 5)
          ssp5 (volume :SSP5 2015 5)

          out1 (volume :SSP1 2020 5)
          out2 (volume :SSP1 2020 10)
          out3 (volume :SSP1 2050 5)]

      ; Assert
      (is (real= (:maximum (:produced (:industrial-emissions ssp1)))
                 [34.436860 36.892236 38.286256 39.680275 40.770526 41.860776
                  42.264693 42.668610 42.853454 43.038298 41.987351 40.936403
                  38.134442 35.332481 32.982921 30.633360 28.841182 27.049004]))
      (is (real= (:maximum (:produced (:industrial-emissions ssp2)))
                 [35.140610 37.148025 39.827663 42.507302 45.124009 47.740715
                  50.677593 53.614471 56.540779 59.467088 62.278246 65.089405
                  69.578691 74.067978 77.672463 81.276948 83.721358 86.165768]))
      (is (real= (:maximum (:produced (:industrial-emissions ssp3)))
                 [36.863002 41.088257 45.123586 49.158915 52.199182 55.239448
                  57.722961 60.206474 62.214590 64.222706 66.351677 68.480648
                  70.604401 72.728153 75.150200 77.572247 80.065546 82.558844]))
      (is (real= (:maximum (:produced (:industrial-emissions ssp4)))
                 [36.532238 40.417488 44.055912 47.694335 49.642782 51.591229
                  52.334744 53.078259 53.385109 53.691960 53.310941 52.929922
                  51.175366 49.420810 47.998013 46.575217 45.452270 44.329322]))
      (is (real= (:maximum (:produced (:industrial-emissions ssp5)))
                 [35.875000 39.550000 45.355000 51.160000 58.500000 65.840000
                  73.985000 82.130000 91.665000 101.200000 109.450000 117.700000
                  123.650000 129.600000 130.250000 130.900000 129.250000
                  127.600000]))

      (is (real= (:maximum (:produced (:industrial-emissions out1)))
                 [36.892236 38.286256 39.680275 40.770526 41.860776 42.264693
                  42.668610 42.853454 43.038298 41.987351 40.936403 38.134442
                  35.332481 32.982921 30.633360 28.841182 27.049004]))
      (is (real= (:maximum (:produced (:industrial-emissions out2)))
                 [36.892236 39.680275 41.860776 42.668610 43.038298 40.936403
                  35.332481 30.633360 27.049004]))
      (is (real= (:maximum (:produced (:industrial-emissions out3)))
                 [42.668610 42.853454 43.038298 41.987351 40.936403 38.134442
                  35.332481 32.982921 30.633360 28.841182 27.049004])))))


;;; test grouping


(deftest parameters-test
  (testing "Some economic parameters based on SSPs:\n"
    (labor)
    (tfp)
    (carbon-intensity)
    (other-parameters)))


;;; tests in the namespace


(defn test-ns-hook
  "Explicit definition of tests in the namespace"
  []
  (parameters-test)
  (volume-test))
