package ru.stqa.pft.github;

import com.google.common.collect.ImmutableMap;
import com.jcabi.github.*;
import org.testng.annotations.Test;

import java.io.IOException;

public class GithubTests {

  @Test
  public void testCommits() throws IOException {
    Github github = new RtGithub("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");
    RepoCommits commits = github.repos().get(new Coordinates.Simple("MElenaV", "java_trening")).commits();
    for (RepoCommit commit: commits.iterate(new ImmutableMap.Builder<String, String>().build())) {  // в скобках iterate описываем условия отбора коммитов; мы хотим получить плный список, но null передавать нельзя, поэтому строим пустой набор пар
      System.out.println(new RepoCommit.Smart(commit).message());
    }
  }
}
