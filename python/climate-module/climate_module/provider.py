import os
import csv
from importlib.resources import open_text
import numpy as np
from . import resources


def parse_emissions(file, transform, n_pars):
    csvreader = csv.reader(file)
    header = next(csvreader)

    ids = []
    values = []
    for row in csvreader:
        ids.append(row[:n_pars])
        values.append(transform(np.array(row[n_pars:], dtype=float)))

    return header, ids, values


def read_emissions(
    resource,
    in_resources=False,
    transform=lambda x: x,
        n_pars=6):

    if in_resources:
        with open_text(resources, resource) as file:
            content = parse_emissions(file, transform, n_pars)
    else:
        with open(resource, 'r', encoding='utf8') as file:
            content = parse_emissions(file, transform, n_pars)

    return content


def read_other_rf_ratio(resource, ssp, ratio):
    file = open_text(resources, resource)
    csvreader = csv.reader(file)

    for row in csvreader:
        if row[0] == ssp and row[1] == ratio:
            other_rf = np.array(row[2:], dtype=float)
            break

    return other_rf


def write_output(args, variable, header, par_ids, data):

    folder = os.path.join('bin', 'scenario ' + args['emissions'])
    if not os.path.exists(folder):
        os.makedirs(folder)

    filename = variable + ' (other_rf ' + args['ratio'] + ').csv'

    with open(
        os.path.join(folder, filename),
        'w',
        newline='',
            encoding='utf8') as file:

        csvwriter = csv.writer(file)
        csvwriter.writerow(header)

        for par_id, values in zip(par_ids, data):
            out = list(map(lambda x: format(x, '.3f'), values))
            csvwriter.writerow([*par_id, *out])
