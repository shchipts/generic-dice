# climate-module

Simulation routines for generic DICE model: climate module



The generic DICE climate module includes:
  * *python implementation of climate module from Hansel et al. (2020)*
  * *dynamic non-CO[2] radiative forcings per SSP scenario*



Simulation routines include:
  * temperature pathways for given SSP(x) net CO[2] emissions (FFI + AFOLU)
  * concentration pathways for given SSP(x) net CO[2] emissions (FFI + AFOLU)

## Install

```cmd
$ pip install -i https://test.pypi.org/simple/ dice-climate-simulator
```

## Usage

  ```
  Usage: dice-climate-simulator -e <emissions> -r <ratio> -f <folder>

  Options:
  -e, --emissions SSP         SSP scenario
  -r, --ratio Z               Non CO2 to CO2 forcings ratio estimate Z
  -f, --folder PATH           PATH to folder with SSP(x) net CO2 emissions pathways
  -h, --help                  Print command help
  ```

### Simulation examples

  * generate climate pathways for SSP2
  ```
  dice-climate-simulator -e SSP2 -r avg -f net-emissions-folder
  ```
  results saved to climate/scenario SSP2/temperature change (other_rf avg).csv
  and climate/scenario SSP2/concentration (other_rf avg).csv

  * generate climate pathways from Hansel et al. (2020)
  ```
  dice-climate-simulator -e hansel2020
  ```
  results saved to scenario climate/hansel2020/temperature change (other_rf avg).csv
  and climate/scenario hansel2020/concentration (other_rf avg).csv

## References

```
[1] Hansel, M., Drupp, M., Johansson, D., Nesje, F., Azar, C., Freeman, M.,
Groom, B., & Sterner, T. (2020). Climate Economics Support for the UN Climate
Targets. Nature Climate Change, 10: 781-789.
https://doi.org/10.1038/s41558-020-0833-x
```

## License

Copyright © 2022 International Institute for Applied Systems Analysis

Licensed under [MIT](http://opensource.org/licenses/MIT)
