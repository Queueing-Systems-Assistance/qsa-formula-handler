# QSA Formula Handler ![CircleCI](https://img.shields.io/circleci/build/github/Queueing-Systems-Assistance/qsa-formula-handler/master)

### Project description

This service is responsible to calculate a system feature formulas. To use the service, please provide the following values in the `build.gradle` file:
```groovy
  githubToken = 'YOUR_TOKEN'
  githubUsername = 'YOUR_USERNAME'
```

### Endpoints

- For tracking set the `X-Request-Id` header value
- For different locales, set the `Accept-Language` header value (en_US, hu_HU, etc.)


#### /formula/default/{systemId}/{featureId}

- Calculates the default formula of the given system feature id
- Accepts only POST requests
- Replace `{systemId}` with a correct systemId (eg. systemMM1)
- Replace `{featureId}` with a correct featureId (eg. Ro)
- Response (example):
```
$\\rho = \\frac{\\lambda}{\\mu}$
```

#### /formula/steps/{systemId}/{featureId}

- Calculates the formula steps of the given system feature id
- Accepts only POST requests
- Replace `{systemId}` with a correct systemId (eg. systemMM1)
- Replace `{featureId}` with a correct featureId (eg. Ro)
- Response (example):
```
$\\bar{Q} = \\sum_{n=1}^{\\infty}(n-1)P_n$<br>We can carry out the multiplication\n$\\bar{Q} = \\sum_{n=1}^{\\infty}(nP_n - P_n)$<br>The result can go under two separate sums\n$\\bar{Q} = \\sum_{n=1}^{\\infty}nP_n - \\sum_{n=1}^{\\infty}P_n$<br>We know that $\\color{blue}{\\bar{N}} = \\sum_{n=1}^{\\infty}nP_n$\nand\n$\\sum_{n=1}^{\\infty}P_n = \\color{orange}{1 - P_0}$\n$\\bar{Q} = \\color{blue}{\\bar{N}} - \\color{orange}{(1-P_0)}$<br>Furthermore $P_0 = \\color{blue}{1 - \\rho}$ so\n$\\bar{Q} = \\bar{N}-(1-\\color{blue}{(1-\\rho)}) = \\bar{N}-\\rho$<br>We also know that $\\bar{N} = \\color{blue}{\\frac{\\rho}{1-\\rho}}$ so\n$\\bar{Q} = \\color{blue}{\\frac{\\rho}{1-\\rho}} - \\rho$<br>$\\bar{Q} = \\frac{\\rho}{1-\\rho} - \\rho = \\frac{\\rho}{1-\\rho} - \\frac{\\rho(1-\\rho)}{1-\\rho} = \\frac{\\rho-\\rho+\\rho^2}{1-\\rho} = \\frac{\\rho^2}{1-\\rho}$
```

#### /formula/calculated/{systemId}/{featureId}

- Calculates the calculated formula of the given system feature id and values
- Accepts only POST requests
- Replace `{systemId}` with a correct systemId (eg. systemMM1)
- Replace `{featureId}` with a correct featureId (eg. Ro)
- Requires a valid JSON body:
```json
{
    "Lambda": 1,
    "Mu": 4,
    "n": 1,
    "r": 1,
    "t": 0.8
}
```
- Response (example):
```
\frac{\left(\frac{1}{4}\right)^2}{1-\frac{1}{4}}
```

### Errors

- If anything goes wrong, this is the response:
  - `400` - BAD_REQUEST: See the error message
  - `404` - NOT_FOUND: wrong endpoint
  - `405` - METHOD_NOT_ALLOWED: wrong HTTP method
  - `406` - NOT_ACCEPTABLE: HTTP body not valid JSON
  - `500` - INTERNAL_SERVER_ERROR: any other error during the request processing
