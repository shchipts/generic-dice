""" Execution command checker.

All rights reserved. The use and distribution terms for this software
are covered by the MIT License (http://opensource.org/licenses/MIT)
which can be found in the file LICENSE at the root of this distribution.
By using this software in any fashion, you are agreeing to be bound by
the terms of this license.
You must not remove this notice, or any other, from this software.
"""

__author__ = "Anna Shchiptsova"
__copyright__ = "Copyright (c) 2022 IIASA"

from getopt import getopt, GetoptError
import sys


def _fail(arg, label, vals):
    """Terminate execution with invalid arguments error message."""
    sys.exit(
        'Failed to validate: ' +
        arg +
        ' not supported ' +
        label +
        '\n(supported ' +
        ' '.join(map(str, vals)) +
        ')')


def parse(argv, options):
    """Parse command line arguments."""
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
