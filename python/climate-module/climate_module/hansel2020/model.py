import functools as func
import numpy as np
from scipy.optimize import root, brentq
from . import parameters as pars


def _co2_to_c(x):
    return x / 3.666


def _init_emissions(e_co2):

    ecum_c = np.zeros(e_co2.size)
    ecum_c[0] = pars.ecum0
    for idx in range(1, e_co2.size):
        ecum_c[idx] = ecum_c[idx - 1] + _co2_to_c(e_co2[idx - 1]) * pars.dt

    return ecum_c


def _iIRF_equation(alpha, ecum_c, c_co2, temp_atm):

    iIRF1 = pars.r0 + pars.rc * (ecum_c - (c_co2 - pars.c_co2_eq)) + \
            pars.rt * temp_atm
    iIRF2 = alpha * (np.sum(pars.a * pars.tau *
                            (1 - np.exp(-100 / (pars.tau * alpha)))))

    return iIRF2 - iIRF1


def _carbon_concentration(carbon_boxes):
    return np.sum(carbon_boxes) + pars.c_co2_eq


def _carbon_cycle(e_co2, ecum_c, c_co2, temp_atm, carbon_boxes):

    alpha = brentq(_iIRF_equation, 0.01, 100, args=(ecum_c, c_co2, temp_atm))

    carbon_boxes_next = np.zeros(carbon_boxes.size)
    for idx1 in range(carbon_boxes.size):
        s = 0
        for idx2 in range(5):
            s += np.exp(- (pars.dt - idx2) / (alpha * pars.tau[idx1]))

        carbon_boxes_next[idx1] = carbon_boxes[idx1] * \
            np.exp(-pars.dt / (alpha * pars.tau[idx1])) + \
            pars.a[idx1] * _co2_to_c(e_co2) * s

    c_co2_next = _carbon_concentration(carbon_boxes_next)

    return c_co2_next, carbon_boxes_next, alpha


def _energy_balance_model(
    c_co2,
    other_rf,
    absolute_other_rf,
    temp_atm,
        temp_lo):

    co2_rf = (pars.kappa / np.log(2.)) * np.log(c_co2 / pars.c_co2_eq)

    if absolute_other_rf:
        total_rf = co2_rf + other_rf
    else:
        total_rf = (1 + other_rf) * co2_rf

    temp_atm_next = func.reduce(
        lambda y, idx: y + 1 / pars.xi1 * (total_rf - pars.xi2 * y - pars.xi3 *
                                           (y - temp_lo)),
        range(4),
        temp_atm)

    temp_lo_next = temp_lo + pars.dt * pars.xi3 / pars.xi4 * \
        (temp_atm - temp_lo)

    return temp_atm_next, temp_lo_next


def climate_module(e_co2, other_rf, absolute_other_rf):

    ecum_c = _init_emissions(e_co2)
    c_co2 = np.zeros(e_co2.size)
    temp_atm = np.zeros(e_co2.size)

    alpha = np.zeros(e_co2.size - 1)

    carbon_boxes = pars.carbon_boxes0
    c_co2[0] = _carbon_concentration(carbon_boxes)
    temp_atm[0] = pars.tat0
    temp_lo = pars.tlo0
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
