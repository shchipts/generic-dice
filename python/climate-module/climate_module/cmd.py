import getopt
import sys

def _fail(arg, label, vals):
    sys.exit( \
        "Failed to validate: " + \
        arg + \
        " not supported " + \
        label + \
        "\n(supported " + \
        ' '.join(map(str, vals)) + \
        ")")

def parse(argv, options):
    help = 'Usage: python -m climate-module' + \
        '-c <climate_module> -i <input> -r <ratio>'

    try:
        opts, args = getopt.getopt(
            argv,
            "hc:i:r:",
            ["climate=", "input=", "ratio="])
    except getopt.GetoptError:
        sys.exit(help)

    if len(opts) != 3:
        sys.exit(help)

    for opt, arg in opts:
        if opt == '-h':
            sys.exit(help)
        elif opt in ("-c", "--climate"):
            module = arg
            if not module in options["module"]:
                _fail(module, "climate module", options["module"])
        elif opt in ("-i", "--input"):
            input = arg
            if not input in options["input"]:
                _fail(input, "input", options["input"])
        elif opt in ("-r", "--ratio"):
            ratio = arg
            if not ratio in options["ratio"]:
                _fail(ratio, "ratio", options["ratio"])
        else:
            sys.exit(help)

    return module, input, ratio
