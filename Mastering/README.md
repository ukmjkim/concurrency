mvn archetype:generate \
          -DinteractiveMode=false \
          -DarchetypeGroupId=org.openjdk.jmh \
          -DarchetypeArtifactId=jmh-java-benchmark-archetype \
          -DgroupId=com.mjkim.concurrency.knn \
          -DartifactId=NearestNeighbors \
          -Dversion=1.0

