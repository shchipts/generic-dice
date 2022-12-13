import sys
from .options import parse
from .provider import read_emissions, read_other_rf_ratio, write_output
from climate_module.hansel2020.inputs import (
    radiative_forcing_non_co2,
    emissions_land_use_co2)
from climate_module.hansel2020.model import climate_module


def _options():
    """Supported command line options."""
    return {
        'emissions': ['hansel2020', 'SSP1', 'SSP2', 'SSP3', 'SSP4', 'SSP5'],
        'ratio': ['avg', 'max', 'min', 'hansel2020']
    }


def _emissions(label, folder):
    if label == 'hansel2020':
        return read_emissions(
            'hansel FFI.csv',
            in_resources=True,
            transform=lambda e: e + emissions_land_use_co2(e.size))

    return read_emissions(folder + '/net-emissions ' + label + '.csv')


def _other_rf(label, ratio):
    if ratio == 'hansel2020':
        return radiative_forcing_non_co2, True

    labels = {'avg': 'Avg Ratio', 'max': 'Max', 'min': 'Min'}
    return read_other_rf_ratio(
        'SSP markers ratio non_CO2 to CO2.csv',
        label,
        labels[ratio]), False


def main(argv):

    args = parse(argv, _options())

    header, par_ids, net_emissions_paths = _emissions(
        args['emissions'],
        args['folder'])
    other_rf, absolute_other_rf = _other_rf(
        args['emissions'],
        args['ratio'])

    climate_output = [[], []]
    for net_emissions in net_emissions_paths:
        temp_atm, concentration = climate_module(
            net_emissions,
            other_rf,
            absolute_other_rf)
        climate_output[0].append(temp_atm)
        climate_output[1].append(concentration)

    for variable, data in zip(
        ['temperature change', 'concentration'],
            climate_output):
        write_output(args, variable, header, par_ids, data)


if __name__ == '__main__':
    main(sys.argv[1:])
