/*
 * Copyright 2021 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openrewrite.java.testing.mockito

import org.junit.jupiter.api.Test
import org.openrewrite.Recipe
import org.openrewrite.java.JavaParser
import org.openrewrite.java.JavaRecipeTest

class MockitoJunitRunnerSilentToExtensionTest : JavaRecipeTest {
    override val parser: JavaParser = JavaParser.fromJavaVersion()
        .classpath("mockito-core", "junit")
        .build()

    override val recipe: Recipe
        get() = MockitoJUnitRunnerSilentToExtension()

    @Test
    fun migrateMockitoRunnerSilentToExtension() = assertChanged(
        before = """
            import org.junit.runner.RunWith;
            import org.mockito.junit.MockitoJUnitRunner;
            
            @RunWith(MockitoJUnitRunner.Silent.class)
            public class ExternalAPIServiceTest {
            }
        """,
        after = """
            import org.junit.jupiter.api.extension.ExtendWith;
            import org.mockito.junit.jupiter.MockitoExtension;
            import org.mockito.junit.jupiter.MockitoSettings;
            import org.mockito.quality.Strictness;
            
            @MockitoSettings(strictness = Strictness.LENIENT)
            @ExtendWith(MockitoExtension.class)
            public class ExternalAPIServiceTest {
            }
        """
    )
}