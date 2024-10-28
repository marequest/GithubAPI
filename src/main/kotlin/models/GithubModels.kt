package org.example.models

import kotlinx.serialization.Serializable

/**
 * All Repositories information.
 */
@Serializable
data class GithubRepositories(
    val total_count: Int,
    val incomplete_results: Boolean,
    val items: List<Repository>
)

/**
 * Single repository information.
 */
@Serializable
data class Repository(
    val id: Long,
    val name: String,
    val full_name: String,
    val default_branch: String? = "master",
    val language: String?
)

/**
 * All files from repository.
 */
@Serializable
data class RepositoryFiles(
    val sha: String,
    val url: String,
    val tree: List<RepoFile>,
    val truncated: Boolean
)

/**
 * File from repository.
 */
@Serializable
data class RepoFile(
    val path: String,
    val mode: String,
    val type: String,
    val sha: String,
    val size: Int? = null,
    val url: String
)