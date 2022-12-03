import numpy as np

"""Climate module parameters (Hansel et al. 2020)

General:
    dt 				years per period


Initial values:
    ecum0 			initial cumulative CO2 emissions (GtCO2)
    tat0 			initial atmospheric temperature change (°C from 1900)
                    (adjusted to only include athropogenic forcing)
    tlo0			initial lower stratum temperature change (°C from 1900)
                    (adjusted to only include athropogenic forcing)


Carbon cycle module (Smith et al. 2018; Hansel et al. 2020):
    r0 				pre-industrial time-integrated airborne fraction of CO2 (yr)
    rc      		change in time-integrated airborne fraction with CO2
                    (yr/GtC)
    rt      		change in time-integrated airborne fraction with temperature
                    (yr/K)
    a       		partition coefficient of carbon boxes
    tau     		present-day decay time constants of CO2 (yr)
    carbon_boxes0	TODO:
    c_co2_eq		equilibrium concentration in atmosphere (GtC)


Energy balance model(Hansel et al. 2020):
    nu 				equilibrium temperature impact (°C per doubling C02)
    kappa			forcings of equilibrium CO2 doubling (Wm-2)
    xi1				speed of adjustment parameter for atmospheric temperature
    xi2				climate model parameter
    xi3				coefficient of heat loss from atmosphere to oceans
    xi4				coefficient of heat gain by deep oceans

References:
    [1] Hansel, M., Drupp, M., Johansson, D., Nesje, F., Azar, C., Freeman, M.,
    Groom, B., & Sterner, T. (2020). Climate Economics Support for the UN
    Climate Targets. Nature Climate Change, 10: 781-789.
    https://doi.org/10.1038/s41558-020-0833-x
    [2] Smith, C., Millar, R., Nicholls, Z., Allen, M., Forster, P., Leach, N.,
    Passerello, G., & Regayre, L. (2018). FAIR - Finite Amplitude Impulse
    Response Model (multi-forcing version).	In Geoscientific Model Development
    (v1.3.2). Zenodo. https://doi.org/10.5281/zenodo.1247898
"""

dt = 5

ecum0 = 400 + 197
tat0 = 1.243
tlo0 = 0.324

r0 = 35.0
rc = 0.019
rt = 4.165
a = np.array([0.217, 0.224, 0.282, 0.276])
tau = np.array([1000000, 394.4, 36.54, 4.304])
carbon_boxes0 = np.array([127.159, 93.313, 37.840, 7.721])
c_co2_eq = 588

nu = 3.1
kappa = 3.6813
xi1 = 7.3
xi2 = kappa / nu
xi3 = 0.73
xi4 = 106
