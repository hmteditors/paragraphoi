# `paragraphoi`

Analysis of paragraphoi and organizing punctuation in Homeric Mss
folio.


## Files in the `chunked-data` directory

Text contents of each paragraphed chunk of the Venetus A (1104 in total), named for the range of lines included in the file.



## Files in the `utils` directory

The files used to create the contents of the `chunked-data` directory.

-   `paragraph-index.csv`: *Iliad* lines are the line *after* the paragraphos mark
-   `iliad.cex`: a text repository with a Perseus *Iliad*, in CEX format
-   `chunkIliad.sc`:  a Scala script to load the text repository, read the index file, and write out a text file for each paragraphed chunk.  You can run it by:
    -  starting a console session with `sbt`:  `sbt console`
    -  loading the script:  `:load chunkIliad.sc`
