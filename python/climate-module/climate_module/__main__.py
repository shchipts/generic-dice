import sys
from .cmd import parse
from .provider import read_input, read_other_rf_ratio, write_output
from climate_module.hansel2020.inputs import (
    radiative_forcing_non_co2,
    emissions_land_use_co2)
from climate_module.hansel2020.model import climate_module as hansel2020


def _options():
    return {
        "module": ["hansel2020"],
        "input": ["hansel2020", "SSP1", "SSP2", "SSP3", "SSP4", "SSP5"],
        "ratio": ["avg", "max", "min", "hansel2020"]
    }


def _climate_module(label):
    if label == "hansel2020":
        return hansel2020


def _input(label, folder):
    if label == "hansel2020":
        return read_input(
            'hansel FFI.csv',
            in_resources=True,
            transform=lambda e: e + emissions_land_use_co2(e.size))
    elif label.startswith('SSP'):
        return read_input(folder + "/net-emissions " + label + ".csv")


def _other_rf(input, ratio):
    if ratio == "hansel2020":
        return radiative_forcing_non_co2, True
    else:
        labels = {"avg": "Avg Ratio", "max": "Max", "min": "Min"}
        return read_other_rf_ratio(
            "SSP markers ratio non_CO2 to CO2.csv",
            input,
            labels[ratio]), False


def main(argv):

    module, input, ratio, folder = parse(argv, _options())

    climate = _climate_module(module)
    header, ids, net_emissions_paths = _input(input, folder)
    other_rf, absolute_other_rf = _other_rf(input, ratio)

    climate_output = [[], []]
    for net_emissions in net_emissions_paths:
        temp_atm, concentration = climate(
            net_emissions,
            other_rf,
            absolute_other_rf)
        climate_output[0].append(temp_atm)
        climate_output[1].append(concentration)

    for variable, data in zip(
        ["temperature change", "concentration"],
            climate_output):
        write_output(input, module, ratio, variable, header, ids, data)


if __name__ == "__main__":
    main(sys.argv[1:])
