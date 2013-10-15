package com.base2.kagura.core.storage;

import com.base2.kagura.core.report.configmodel.ReportsConfig;
import com.google.common.io.PatternFilenameFilter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author aubels
 *         Date: 15/10/13
 */
public class GitReportsProvider extends ReportsProvider<File> {
    String gitRepository;
    String gitBranch;
    String username;
    String password;
    Git git;

    public GitReportsProvider(String gitRepository, String gitBranch) {
        this.gitRepository = gitRepository;
        this.gitBranch = gitBranch;
        this.username = null;
        this.password = null;
        this.git = cloneGit(gitRepository, gitBranch);
    }

    private Git cloneGit(String gitRepository, String gitBranch) {
        CloneCommand cloneCommand = new CloneCommand().setRemote("origin").setURI(gitRepository)
                .setBranch(gitBranch);
        if (StringUtils.isNotBlank(username))
        {
            CredentialsProvider credentails = new UsernamePasswordCredentialsProvider(username, password);
            cloneCommand.setCredentialsProvider(credentails);
        }
        try {
            return cloneCommand.call();
        } catch (GitAPIException e) {
            e.printStackTrace();
            errors.add("GIT error: " + e.getMessage());
        }

        return null;
    }

    @Override
    protected String loadReport(ReportsConfig result, File report) throws Exception {
        if (report.isDirectory())
        {
            FilenameFilter configYamlFilter = new PatternFilenameFilter("^reportconf.(yaml|json)$");
            File[] selectYaml = report.listFiles(configYamlFilter);
            if (selectYaml != null && selectYaml.length == 1)
            {
                File selectedYaml = selectYaml[0];
                if (loadReport(result, FileUtils.openInputStream(selectedYaml), report.getName()))
                    return report.getName();
            }
        }
        return report.getName();
    }

    @Override
    protected File[] getReportList() {
        return git.getRepository().getDirectory().listFiles();
    }
}
