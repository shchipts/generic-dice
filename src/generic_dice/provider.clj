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
  (:require [clojure.edn :as edn]
            [clojure.set :as s]
            [utilities-clj.format :as formatter]))
