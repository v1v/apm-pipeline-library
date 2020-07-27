#!/usr/bin/env groovy

/**
* Given the YAML definition and the changeset global macros
* then it verifies if the project or stage should be enabled.
*/
Boolean when(Map args = [:]) {
  def project = args.project
  def content = args.content
  def patterns = args.changeset
  def ret = false

  if (whenComments(args)) { ret = true }
  if (whenLabels(args)) { ret = true }
  if (whenParameters(args)) { ret = true }
  if (whenBranches(args)) { ret = true }
  if (whenTags(args)) { ret = true }
  // TODO: changeset validation
  return ret
}

private Boolean whenBranches(Map args = [:]) {
  def ret = false
  if (env.BRANCH_NAME?.trim() && args.content?.get('branches')) {
    ret = true
    markdownReason(project: args.project, reason: 'Branch is enabled and matches with the pattern.')
  }
  return ret
}

private Boolean whenComments(Map args = [:]) {
  def ret = false
  if (args.content?.get('comments') && env.GITHUB_COMMENT?.trim()) {
    if (args.content?.get('comments')?.any { env.GITHUB_COMMENT?.toLowerCase()?.contains(it?.toLowerCase()) }) {
      ret = true
      markdownReason(project: args.project, reason: 'Comment is enabled and matches with the pattern.')
    } else {
      markdownReason(project: args.project, reason: 'Comment is enabled and does not match with the pattern.')
    }
  }
  return ret
}

private Boolean whenLabels(Map args = [:]) {
  def ret = false
  if (args.content?.get('labels')) {
    if (args.content?.get('labels')?.any { matchesPrLabel(label: it) }) {
      ret = true
      markdownReason(project: args.project, reason: 'Label is enabled and matches with the pattern.')
    } else {
      markdownReason(project: args.project, reason: 'Label is enabled and does not match with the pattern.')
    }
  }
  return ret
}

private Boolean whenParameters(Map args = [:]) {
  def ret = false
  if (args.content?.get('parameters')) {
    if (args.content?.get('parameters')?.any { params[it] }) {
      ret = true
      markdownReason(project: args.project, reason: 'Parameter is enabled and matches with the pattern.')
    } else {
      markdownReason(project: args.project, reason: 'Parameter is enabled and does not match with the pattern.')
    }
  }
  return ret
}

private Boolean whenTags(Map args = [:]) {
  def ret = false
  if (env.TAG_NAME?.trim() && args.content?.get('tags')) {
    ret = true
    markdownReason(project: args.project, reason: 'Tag is enabled and matches with the pattern.')
  }
  return ret
}

private void markdownReason(Map args = [:]) {
  echo "${args.project} - ${args.reason}"
  // TODO create markdown
}
