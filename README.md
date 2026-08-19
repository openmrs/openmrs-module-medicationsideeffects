# OpenMRS Medication Side Effects Module

Backend module that stores structured **medication side-effect metadata** and exposes it over a
read-only REST API. Each record links a side effect to a `Drug` (at the drug/formulation level),
classifies it as *common* or *serious*, describes it either by a coded `Concept` or as free text,
and may carry a recommended clinical action.

The data is intended to be consumed by O3 frontends — see the
[`openmrs-esm-medication-side-effects`](https://github.com/openmrs/openmrs-esm-medication-side-effects)
microfrontend, which renders the side-effects panel on the drug order form and the dispensing screen.

## Data model

Table `medication_side_effect` (entity `MedicationSideEffect`, extends `BaseOpenmrsData`, soft-deletable):

| Column                    | Description                                                                 |
| ------------------------- | --------------------------------------------------------------------------- |
| `drug_id`                 | Required FK to `drug`. The drug the side effect belongs to.                 |
| `classification`          | Required. `COMMON` or `SERIOUS`.                                            |
| `side_effect_concept_id`  | FK to `concept` — the coded effect (typically for common effects).          |
| `side_effect_text`        | Free-text effect (typically for serious effects).                           |
| `recommended_action`      | Optional free-text recommended clinical action.                             |
| `notes`                   | Optional internal notes.                                                    |

At least one of `side_effect_concept_id` or `side_effect_text` must be present on each row.

## REST API

Read-only resource, discovered by the `webservices.rest` module. The only supported access pattern is
a search by drug:

```
GET /ws/rest/v1/medicationsideeffect?drug={drugUuid}
```

Returns every non-voided side effect linked to the given drug. A single record is also retrievable by
uuid (`GET /ws/rest/v1/medicationsideeffect/{uuid}`). The `default`/`ref` representations expose:

```json
{
  "results": [
    {
      "uuid": "…",
      "display": "Nausea",
      "classification": "COMMON",
      "sideEffectText": null,
      "recommendedAction": null,
      "notes": "…",
      "drug": { "uuid": "…", "display": "Acetaminophen 325 mg" },
      "sideEffectConcept": { "uuid": "…", "display": "Nausea" },
      "voided": false
    }
  ]
}
```

`display` resolves to the concept's display name when a coded concept is set, otherwise the free text.
Creating, updating and deleting records over REST is not supported (the resource is read-only).

Privileges: `Get Medication Side Effects` (read) and `Manage Medication Side Effects` (write, used by
the service layer).

## Loading data

Records are not authored through the REST API. They are loaded declaratively via the
[Initializer](https://github.com/openmrs/openmrs-module-initializer) module's **`medicationsideeffects`**
domain — a CSV placed in the Initializer configuration directory
(`configuration/medicationsideeffects/medication_side_effects.csv`). See
`docs/sample-configuration/` for an example and the Initializer domain documentation for the column
reference.

## Requirements

- OpenMRS Platform **2.4.0** or higher
- `webservices.rest` module
- `initializer` module (to load the CSV data)

## Building

```sh
mvn clean install
```

This produces `omod/target/medicationsideeffects-<version>.omod`, which can be installed via the
OpenMRS Manage Modules page or dropped into the server's modules directory.

## License

This module is licensed under the [MPL 2.0 with Healthcare Disclaimer](http://openmrs.org/license).
