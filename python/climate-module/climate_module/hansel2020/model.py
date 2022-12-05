from functools import reduce
import numpy as np
from scipy.optimize import brentq
from . import parameters as pars


def _co2_to_c(co2):
    return co2 / 3.666


def _init_emissions(e_co2):

    ecum_c = np.zeros(e_co2.size)
    ecum_c[0] = pars.ECUM0
    for idx in range(1, e_co2.size):
        ecum_c[idx] = ecum_c[idx - 1] + _co2_to_c(e_co2[idx - 1]) * pars.DT

    return ecum_c


def _iirf_equation(alpha, ecum_c, c_co2, temp_atm):

    iirf1 = pars.R0 + pars.RC * (ecum_c - (c_co2 - pars.C_CO2_EQ)) + \
            pars.RT * temp_atm
    iirf2 = alpha * (np.sum(pars.A * pars.TAU *
                            (1 - np.exp(-100 / (pars.TAU * alpha)))))

    return iirf2 - iirf1


def _carbon_concentration(carbon_boxes):
    return np.sum(carbon_boxes) + pars.C_CO2_EQ


def _carbon_cycle(e_co2, ecum_c, c_co2, temp_atm, carbon_boxes):

    alpha = brentq(_iirf_equation, 0.01, 100, args=(ecum_c, c_co2, temp_atm))

    carbon_boxes_next = np.zeros(carbon_boxes.size)
    for idx1 in range(carbon_boxes.size):
        step = 0
        for idx2 in range(5):
            step += np.exp(- (pars.DT - idx2) / (alpha * pars.TAU[idx1]))

        carbon_boxes_next[idx1] = carbon_boxes[idx1] * \
            np.exp(-pars.DT / (alpha * pars.TAU[idx1])) + \
            pars.A[idx1] * _co2_to_c(e_co2) * step

    c_co2_next = _carbon_concentration(carbon_boxes_next)

    return c_co2_next, carbon_boxes_next, alpha


def _energy_balance_model(
    c_co2,
    other_rf,
    absolute_other_rf,
    temp_atm,
        temp_lo):

    co2_rf = (pars.KAPPA / np.log(2.)) * np.log(c_co2 / pars.C_CO2_EQ)

    if absolute_other_rf:
        total_rf = co2_rf + other_rf
    else:
        total_rf = (1 + other_rf) * co2_rf

    temp_atm_next = reduce(
        lambda y, idx: y + 1 / pars.XI1 * (total_rf - pars.XI2 * y - pars.XI3 *
                                           (y - temp_lo)),
        range(4),
        temp_atm)

    temp_lo_next = temp_lo + pars.DT * pars.XI3 / pars.XI4 * \
        (temp_atm - temp_lo)

    return temp_atm_next, temp_lo_next


def climate_module(e_co2, other_rf, absolute_other_rf):

    ecum_c = _init_emissions(e_co2)
    c_co2 = np.zeros(e_co2.size)
    temp_atm = np.zeros(e_co2.size)

    alpha = np.zeros(e_co2.size - 1)

    carbon_boxes = pars.CARBON_BOXES0
    c_co2[0] = _carbon_concentration(carbon_boxes)
    temp_atm[0] = pars.TAT0
    temp_lo = pars.TLO0
    for idx in range(e_co2.size - 1):
        c_co2[idx + 1], carbon_boxes, alpha[idx] = _carbon_cycle(
            e_co2[idx],
            ecum_c[idx],
            c_co2[idx],
            temp_atm[idx],
            carbon_boxes)
        temp_atm[idx + 1], temp_lo = _energy_balance_model(
            c_co2[idx + 1],
            other_rf[idx + 1],
            absolute_other_rf,
            temp_atm[idx],
            temp_lo)

    return temp_atm, c_co2
