# Notes to self

## Code snippets in javadoc

```
This method is equivalent to
<blockquote><pre>
if (v == null) {
    throw new NullPointerException();
}
return vertexSet().contains(v);
</pre></blockquote>
<p> <!-- new paragraph if the following content is unrelated -->
More text below the block.
```
