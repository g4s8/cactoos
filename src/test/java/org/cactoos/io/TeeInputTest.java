/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.cactoos.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.cactoos.TextHasString;
import org.cactoos.func.FuncAsMatcher;
import org.cactoos.text.BytesAsText;
import org.cactoos.text.StringAsText;
import org.cactoos.text.TextAsBytes;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

/**
 * Test case for {@link TeeInput}.
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class TeeInputTest {

    @Test
    public void copiesContent() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final String content = "Hello, товарищ!";
        MatcherAssert.assertThat(
            "Can't copy Input to Output and return Input",
            new BytesAsText(
                new InputAsBytes(
                    new TeeInput(
                        new BytesAsInput(
                            new TextAsBytes(content)
                        ),
                        new OutputStreamAsOutput(baos)
                    )
                )
            ),
            new TextHasString(
                new FuncAsMatcher<>(
                    str -> new String(
                        baos.toByteArray(), StandardCharsets.UTF_8
                    ).equals(str)
                )
            )
        );
    }

    @Test
    public void copiesToFile() throws IOException {
        final Path temp = Files.createTempFile("cactoos", "txt");
        MatcherAssert.assertThat(
            "Can't copy Input to File and return content",
            new BytesAsText(
                new InputAsBytes(
                    new TeeInput(
                        new BytesAsInput(
                            new TextAsBytes(
                                new StringAsText("Hello, друг!")
                            )
                        ),
                        new PathAsOutput(temp)
                    )
                )
            ),
            new TextHasString(
                new FuncAsMatcher<>(
                    str -> str.equals(
                        new String(
                            Files.readAllBytes(temp),
                            StandardCharsets.UTF_8
                        )
                    )
                )
            )
        );
    }
}
