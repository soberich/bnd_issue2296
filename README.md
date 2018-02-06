# bnd_issue2296

`gradlew build`

produces the proper wab/war-alike archive with all transitive runtime deps in the archive


if in bnd.bnd file try to change the `lastDep` varible to any of the ones with `replace` `subst` macroses it won't pars the list (string) properly for some reason. 
this version is the simpliest as originally the agenda was to parse each dep (with `foreach` or `apply`) which did not work with the samу error. So now I just artificially cut out the last `]` to include the dep which is throughen away as it end with `.jar]` and not wit just `.jar`.

```
deps: ${project.sourceSets.main.runtimeClasspath.files}
# HERE!! these two does not work
#lastDep: {subst;{last;{deps}};(^\\[)|(\\]\$)}   does not work
#lastDep: {replace;{last;{deps}};\\];}             does not work
lastDep: ${substring;${last;${deps}};0;-1}
-wablib: ${format;%s,;${filter;${deps},${lastDep};.*\.jar$}}
```
