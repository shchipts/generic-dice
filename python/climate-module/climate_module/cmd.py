from getopt import getopt, GetoptError
import sys


def _fail(arg, label, vals):
    sys.exit(
        'Failed to validate: ' +
        arg +
        ' not supported ' +
        label +
        '\n(supported ' +
        ' '.join(map(str, vals)) +
        ')')


def parse(argv, options):
    help_msg = 'Usage: python -m climate-module' + \
        ' -e <emissions> -r <ratio> -f <folder>'

    try:
        opts, _ = getopt(
            argv,
            'h:e:r:f:',
            ['emissions=', 'ratio=', 'folder='])
    except GetoptError:
        sys.exit(help_msg)

    if len(opts) < 3:
        sys.exit(help_msg)

    args = {}
    args['folder'] = None
    for opt, arg in opts:
        if opt == '-h':
            sys.exit(help)
        if opt in ('-e', '--emissions'):
            if arg not in options['emissions']:
                _fail(arg, 'emissions', options['emissions'])
            args['emissions'] = arg
        if opt in ('-r', '--ratio'):
            if arg not in options['ratio']:
                _fail(arg, 'ratio', options['ratio'])
            args['ratio'] = arg
        if opt in ('-f', '--folder'):
            args['folder'] = arg

    if len(args) < 3:
        sys.exit(help)

    return args
