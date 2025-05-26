cmake_minimum_required(VERSION 3.16)
project(MatchingEngine)

set(CMAKE_CXX_STANDARD 17)

# Include source files
file(GLOB SOURCES "src/**/*.cpp")
add_executable(MatchingEngine ${SOURCES})

# Protobuf
find_package(Protobuf REQUIRED)
include_directories(${Protobuf_INCLUDE_DIRS})
add_library(proto
    ${CMAKE_SOURCE_DIR}/include/dto/order.pb.cc
    ${CMAKE_SOURCE_DIR}/include/dto/order.pb.h
)
target_include_directories(proto PUBLIC ${Protobuf_INCLUDE_DIRS})
target_link_libraries(proto ${Protobuf_LIBRARIES})

# Include your headers
include_directories(include)

# 🔧 Manually include librdkafka
include_directories(/opt/homebrew/Cellar/librdkafka/2.10.0/include)
include_directories(/opt/homebrew/Cellar/librdkafka/2.10.0/include/librdkafka)
link_directories(/opt/homebrew/lib)
link_directories(/opt/homebrew/Cellar/librdkafka/2.10.0/lib)

# Add Abseil library for Protobuf dependencies
find_package(absl CONFIG REQUIRED)

# 🔗 Link everything together
target_link_libraries(MatchingEngine
    PRIVATE proto
    PRIVATE ${Protobuf_LIBRARIES}
    PRIVATE /opt/homebrew/Cellar/librdkafka/2.10.0/lib/librdkafka.dylib
)

# Link Abseil to the target
if (absl_FOUND)
    target_link_libraries(MatchingEngine PRIVATE absl::base absl::strings)
endif()
