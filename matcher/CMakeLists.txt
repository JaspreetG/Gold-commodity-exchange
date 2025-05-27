cmake_minimum_required(VERSION 3.16)
project(MatchingEngine)
set(CMAKE_CXX_STANDARD 17)

# Recursively grab all .cpp under src/
file(GLOB_RECURSE SOURCES
    CONFIGURE_DEPENDS
    "${CMAKE_CURRENT_SOURCE_DIR}/src/*.cpp"
)

add_executable(MatchingEngine ${SOURCES})

# Include headers
include_directories(include)

# librdkafka
include_directories(
  /opt/homebrew/Cellar/librdkafka/2.10.0/include
  /opt/homebrew/Cellar/librdkafka/2.10.0/include/librdkafka
)
link_directories(
  /opt/homebrew/lib
  /opt/homebrew/Cellar/librdkafka/2.10.0/lib
)

# nlohmann-json
include_directories(/opt/homebrew/include)

# Link librdkafka
target_link_libraries(MatchingEngine PRIVATE
    /opt/homebrew/Cellar/librdkafka/2.10.0/lib/librdkafka.dylib
)
