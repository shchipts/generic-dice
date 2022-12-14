# generic-dice

Contains simulation routines for generic DICE model

* `clj/generic-dice`: performs economy simulations from generic DICE model [economy module]
* `python/climate-module`: performs climate simulations from generic DICE model [climate module]

The generic DICE economy module includes:
  * *constraints based on limiting case of massive CDR deployment*
  * *emissions quota for global-mean warming below 3°C*
  * *dynamic AFOLU emissions*

The generic DICE climate module includes:
  * *python implementation of climate module from Hansel et al. (2020)*
  * *dynamic non-CO2 radiative forcings per SSP scenario*

## References

```
[1] Nordhaus, W. (2017). Revisiting the social cost of carbon. PNAS, 114(7): 1518-1523. https://doi.org/10.1073/pnas.1609244114
[2] Kriegler, E., Luderer, G., Bauer, N., Baumstark, L., Fujimori, S., Popp, A., Rogelj, J., Strefler, J., & van Vuuren, D. (2018). Pathways Limiting Warming To 1.5°C: A Tale Of Turning Around In No Time?. Philosophical Transactions A, 376: 20160457. https://doi.org/10.1098/rsta.2016.0457
[3] Friedlingstein, P., Andrew, R., Rogelj, J., Peters, G., Canadell, J., Knutti, R., Luderer, G., Raupach, M., Schaeffer, M., van Vuuren, D., & Le Quere, C. (2014). Persistent Growth of CO2 Emissions and Implications for Reaching Climate Targets. Nature Geoscience, 7: 709–715. https://doi.org/10.1038/ngeo2248
[4] Hansel, M., Drupp, M., Johansson, D., Nesje, F., Azar, C., Freeman, M., Groom, B., & Sterner, T. (2020). Climate Economics Support for the UN Climate Targets. Nature Climate Change, 10: 781-789.
https://doi.org/10.1038/s41558-020-0833-x
```

## License

Copyright © 2022 International Institute for Applied Systems Analysis

Licensed under [MIT](http://opensource.org/licenses/MIT)
