package com.cleancompose.domain.usecases

import com.cleancompose.domain.models.DomainModelFactory.getDefaultPostModel
import com.cleancompose.domain.repositories.PostsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.UUID

class GetPostUseCaseTest {
    private val postRepository: PostsRepository = mockk()
    private val getPostUseCase = GetPostUseCase(postRepository)

    @Test
    fun `Given repository returns full list, When GetPostUseCase is invoked, Then returns full list`() =
        runTest {
            // Given
            val expectedPostsList = List(23) {
                getDefaultPostModel(UUID.randomUUID().toString())
            }

            coEvery { postRepository.getPosts(0) } returns flow { emit(expectedPostsList) }

            // When
            val actualPostsList = getPostUseCase.invoke(0).last()

            // Then
            assertEquals(expectedPostsList, actualPostsList)
        }

    @Test
    fun `Given repository returns empty list, When GetPostUseCase is invoked, Then returns empty list`() =
        runTest {
            // Given
            coEvery { postRepository.getPosts(0) } returns flow { emit(emptyList()) }

            // When
            val postsList = getPostUseCase.invoke(0).last()

            // Then
            assertTrue(postsList.isEmpty())
        }
}