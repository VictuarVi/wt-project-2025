#import "lib.typ": *
#import "properties.typ": *

#set-theme(yaml("classic-light.yaml"))

#show: project.with(..properties)

#show: frontmatter.with()
#include "sections/abstract.typ"

#show: mainmatter.with()
#include "sections/original_submission.typ"
#include "sections/submission.typ"
#include "sections/specification_completion.typ"
#include "sections/sql.typ"
#include "sections/components.typ"
#include "sections/sequence_diagram.typ"
#include "sections/filters.typ"

#show: appendix.with()
#include "sections/css_rules.typ"
#include "sections/cut_content.typ"

#bibliography(
  "bibliography.yml",
  style: "ieee",
  full: false,
)
