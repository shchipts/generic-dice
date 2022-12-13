""" Climate module parameters (Hansel et al. 2020).


General:

.. parameters:: DT

    Indicates years per period.


Initial values:

.. parameters:: ECUM0

    Indicates initial cumulative CO2 emissions (GtCO2).

.. parameters:: TAT0

    Indicates initial atmospheric temperature change (°C from 1900)
    (adjusted to only include athropogenic forcing).

.. parameters:: TLO0

    Indicates initial lower stratum temperature change (°C from 1900)
    (adjusted to only include athropogenic forcing).


Carbon cycle module (Smith et al. 2018; Hansel et al. 2020):

.. parameters:: R0

    Indicates pre-industrial time-integrated airborne fraction of CO2 (yr).

.. parameters:: RC

    Indicates change in time-integrated airborne fraction with CO2 (yr/GtC).

.. parameters:: RT

    Indicates change in time-integrated airborne fraction with temperature
    (yr/K).

.. parameters:: A

    Indicates partition coefficient of carbon boxes.

.. parameters:: TAU

    Indicates present-day decay time constants of CO2 (yr).

.. parameters:: CARBON_BOXES0

    Indicates carbon cycle parameters.

.. parameters:: C_CO2_EQ

    Indicates equilibrium concentration in atmosphere (GtC).


Energy balance model(Hansel et al. 2020):

.. parameters:: NU

    Indicates equilibrium temperature impact (°C per doubling C02).

.. parameters:: KAPPA

    Indicates forcings of equilibrium CO2 doubling (Wm-2).

.. parameters:: XI1

    Indicates speed of adjustment parameter for atmospheric temperature.

.. parameters:: XI2

    Indicates climate model parameter.

.. parameters:: XI3

    Indicates coefficient of heat loss from atmosphere to oceans.

.. parameters:: XI4

    Indicates coefficient of heat gain by deep oceans.

References:
    [1] Hansel, M., Drupp, M., Johansson, D., Nesje, F., Azar, C., Freeman, M.,
    Groom, B., & Sterner, T. (2020). Climate Economics Support for the UN
    Climate Targets. Nature Climate Change, 10: 781-789.
    https://doi.org/10.1038/s41558-020-0833-x
    [2] Smith, C., Millar, R., Nicholls, Z., Allen, M., Forster, P., Leach, N.,
    Passerello, G., & Regayre, L. (2018). FAIR - Finite Amplitude Impulse
    Response Model (multi-forcing version).	In Geoscientific Model Development
    (v1.3.2). Zenodo. https://doi.org/10.5281/zenodo.1247898

All rights reserved. The use and distribution terms for this software
are covered by the MIT License (http://opensource.org/licenses/MIT)
which can be found in the file LICENSE at the root of this distribution.
By using this software in any fashion, you are agreeing to be bound by
the terms of this license.
You must not remove this notice, or any other, from this software.
"""

__author__ = "Anna Shchiptsova"
__copyright__ = "Copyright (c) 2022 IIASA"

import numpy as np


DT = 5

ECUM0 = 400 + 197
TAT0 = 1.243
TLO0 = 0.324

R0 = 35.0
RC = 0.019
RT = 4.165
A = np.array([0.217, 0.224, 0.282, 0.276])
TAU = np.array([1000000, 394.4, 36.54, 4.304])
CARBON_BOXES0 = np.array([127.159, 93.313, 37.840, 7.721])
C_CO2_EQ = 588

NU = 3.1
KAPPA = 3.6813
XI1 = 7.3
XI2 = KAPPA / NU
XI3 = 0.73
XI4 = 106
