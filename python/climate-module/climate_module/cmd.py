import getopt
import sys


def _fail(arg, label, vals):
    sys.exit(
        "Failed to validate: " +
        arg +
        " not supported " +
        label +
        "\n(supported " +
        ' '.join(map(str, vals)) +
        ")")


def parse(argv, options):
    help = 'Usage: python -m climate-module' + \
        ' -c <climate_module> -i <input> -r <ratio> -f <folder>'

    try:
        opts, args = getopt.getopt(
            argv,
            "hc:i:r:f:",
            ["climate=", "input=", "ratio=", "folder="])
    except getopt.GetoptError:
        sys.exit(help)

    if len(opts) < 3:
        sys.exit(help)

    folder = None

    for opt, arg in opts:
        if opt == '-h':
            sys.exit(help)
        elif opt in ("-c", "--climate"):
            module = arg
            if module not in options["module"]:
                _fail(module, "climate module", options["module"])
        elif opt in ("-i", "--input"):
            input = arg
            if input not in options["input"]:
                _fail(input, "input", options["input"])
        elif opt in ("-r", "--ratio"):
            ratio = arg
            if ratio not in options["ratio"]:
                _fail(ratio, "ratio", options["ratio"])
        elif opt in ("-f", "--folder"):
            folder = arg
        else:
            sys.exit(help)

    return module, input, ratio, folder
