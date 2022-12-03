import csv
import importlib.resources as pkg_resources
import numpy as np
import os
from . import resources


def read_input(
    resource,
    in_resources=False,
    transform=lambda x: x,
        n_pars=6):

    if in_resources:
        file = pkg_resources.open_text(resources, resource)
    else:
        file = open(resource, "r")

    csvreader = csv.reader(file)

    header = next(csvreader)

    ids = []
    values = []
    for row in csvreader:
        ids.append(row[:n_pars])
        values.append(transform(np.array(row[n_pars:], dtype=float)))

    file.close()

    return header, ids, values


def read_other_rf_ratio(resource, ssp, ratio):
    file = pkg_resources.open_text(resources, resource)
    csvreader = csv.reader(file)

    header = next(csvreader)

    for row in csvreader:
        if row[0] == ssp and row[1] == ratio:
            other_rf = np.array(row[2:], dtype=float)
            break

    return other_rf


def write_output(input, module, ratio, variable, header, ids, data):

    folder = os.path.join("bin", module, "scenario " + input)
    if not os.path.exists(folder):
        os.makedirs(folder)

    filename = variable + " (other_rf " + ratio + ").csv"

    with open(os.path.join(folder, filename), 'w', newline='') as file:
        csvwriter = csv.writer(file)
        csvwriter.writerow(header)
        for id, values in zip(ids, data):
            out = list(map(lambda x: format(x, '.3f'), values))
            csvwriter.writerow([*id, *out])
