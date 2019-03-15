package octopus.teamcity.agent;

import jetbrains.buildServer.agent.BuildProgressLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitHubCommentParser extends CommentParser {
    private static final String GITHUB_ID_REGEX = "(?:close[d|s]*|fix[ed|es]*|resolve[d|s]*):?\\s((?:#|GH-|http[/A-Z:.]*/issues/)(\\d+))";

    public String getIssueTrackerSuffix() {
        return "github";
    }

    public List<WorkItem> parse(final String comment, final BuildProgressLogger buildLogger) {
        buildLogger.message("Parsing comments for GitHub issues");
        final List<WorkItem> workItems = new ArrayList<WorkItem>();

        final Pattern githubId = Pattern.compile(GITHUB_ID_REGEX, Pattern.CASE_INSENSITIVE);
        final Matcher githubMatcher = githubId.matcher(comment);

        while (githubMatcher.find()) {
            final WorkItem workItem = new WorkItem();
            final String linkData = githubMatcher.group(1);
            final String issueNumber = githubMatcher.group(2);

            workItem.Id = issueNumber;
            workItem.LinkData = linkData;
            workItem.LinkText = issueNumber;

            buildLogger.message("Located GitHub issue " + workItem.LinkData);

            workItems.add(workItem);
        }

        return workItems;
    }
}