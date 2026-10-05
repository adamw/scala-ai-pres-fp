package pres

class S310_An_Orca_script:
  import orca.{*, given}

  flow(OrcaArgs(Array.empty[String])):
    val plan = stage("Plan"):
      Plan.autonomous.from(userPrompt, planningAgent).value

    val session = codingAgent.session("implementer", seed = plan.brief)

    val taskFindings =
      for task <- plan.tasks yield stage(s"Task: ${task.title}"):
        session.run(task.description)
        reviewThenFix(
          coderSession = session,
          reviewers = allReviewers(reviewAgent),
          task = task
        )

    val openFindings = stage("Final review"):
      reviewAndFixLoop(
        coderSession = session,
        reviewers = allReviewers(reviewAgent),
        task = Task(Title("The whole planned change"), plan.brief),
        diff = ReviewDiff.WholeRun,
        maxIterations = 5,
        priorOpenFindings = OpenFindings(taskFindings.flatMap(_.findings))
      )

    openPrIfGitHub(
      summarisingAgent = codingAgent.cheap,
      openFindings = openFindings
    )
