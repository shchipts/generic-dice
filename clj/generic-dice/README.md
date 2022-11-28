# generic-dice

Performs simulations from generic DICE model (economic module)

## Install

No installation needed. This is a standalone java application.
Requires Java SE 14.

## Usage

  execute in the root directory

  $ lein run

  ```
  Options:
  -c, --command NAME          :net-emissions-ffi   What to simulate from the model?
  -z, --time-step Z           5                    Time step Z
  -s, --start-year Y          2015                 Start year Y
  -e, --end-year Y            2100                 End year Y
  -i, --initial-values MODEL  DICE2016             MODEL instance: initial values
  -d, --damages MODEL         :howard-sterner2017  MODEL instance: damage function
  -a, --costs MODEL           :dice2016            MODEL instance: abatement cost function
  -p, --scenario MODEL        :SSP1                MODEL instance: SSP scenario
  -f, --input FILE                                 Path to FILE with simulation input data
  -r, --folder PATH           bin                  PATH to folder for output writing
  -o, --options                                    Print options to file
  -t, --trace                                      Print stack trace
  -h, --help                                       Print command help
  ```

### Simulation examples

  * generate net-emissions for all SSPs
  ```
  lein run -c net-emissions-ffi
  ```
  results saved to PATH/net-emissions SSPx.csv

  * generate CDR emissions (SSP independent)
  ```
  lein run -c cdr
  ```
  results saved to PATH/cdr-emissions.csv

  * generate SSP(x) economic curves for temperature.csv with assumed damages and abatement costs
  ```
  lein run -c economy -d burke2015 -a su2017 -p SSP2 -f temperature.csv
  ```
  results saved to "PATH/damages burke2015 costs su2017"

## License

Copyright © 2022 International Institute for Applied Systems Analysis

Licensed under [MIT](http://opensource.org/licenses/MIT)
