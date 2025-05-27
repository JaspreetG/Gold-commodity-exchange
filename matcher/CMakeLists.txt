cmake_minimum_required(VERSION 3.16)
project(MatchingEngine)

set(CMAKE_CXX_STANDARD 17)

# Include source files
file(GLOB SOURCES "src/**/*.cpp")

# Explicitly add strategy source files to ensure they are compiled
file(GLOB STRATEGY_SOURCES "src/core/strategies/*.cpp")

add_executable(MatchingEngine ${SOURCES} ${STRATEGY_SOURCES})

# Include your headers
include_directories(include)

# 🔧 Manually include librdkafka
include_directories(/opt/homebrew/Cellar/librdkafka/2.10.0/include)
include_directories(/opt/homebrew/Cellar/librdkafka/2.10.0/include/librdkafka)
link_directories(/opt/homebrew/lib)
link_directories(/opt/homebrew/Cellar/librdkafka/2.10.0/lib)

# Add nlohmann-json include directory (Homebrew default)
include_directories(/opt/homebrew/include)

# 🔗 Link everything together
target_link_libraries(MatchingEngine
    PRIVATE /opt/homebrew/Cellar/librdkafka/2.10.0/lib/librdkafka.dylib
)
