# Notes to self

## Code snippets in javadoc

```
This method is equivalent to
<pre><code>
if (v == null) {
    throw new NullPointerException();
}
return vertexSet().contains(v);
</code></pre>
<p> <!-- new paragraph if the following content is unrelated -->
More text below the block.
```
