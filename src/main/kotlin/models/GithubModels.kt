package org.example.models

import kotlinx.serialization.Serializable

@Serializable
data class GitHubRepo(
    val total_count: Int,
    val incomplete_results: Boolean,
    val items: List<Repository>
)

@Serializable
data class Repository(
    val id: Long,
    val name: String,
    val full_name: String,
    val default_branch: String? = "master",
    val language: String?
)

@Serializable
data class TreeResponse(
    val sha: String,
    val url: String,
    val tree: List<TreeItem>,
    val truncated: Boolean
)

@Serializable
data class TreeItem(
    val path: String,
    val mode: String,
    val type: String,
    val sha: String,
    val size: Int? = null,
    val url: String
)