for i in `git log $1..HEAD | grep 'Fixes #' | awk  '{print substr($2,2)}'`
do
	echo "$i " `curl -s https://api.github.com/repos/redhat-developer/intellij-openshift-connector/issues/$i | jq '.title'`
done
