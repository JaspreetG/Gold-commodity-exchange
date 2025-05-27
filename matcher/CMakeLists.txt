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

# macOS/Homebrew-specific settings
if(APPLE)
    include_directories(
      /opt/homebrew/Cellar/librdkafka/2.10.0/include
      /opt/homebrew/Cellar/librdkafka/2.10.0/include/librdkafka
    )
    link_directories(
      /opt/homebrew/lib
      /opt/homebrew/Cellar/librdkafka/2.10.0/lib
    )
    include_directories(/opt/homebrew/include)
    set(OPENSSL_ROOT_DIR "/opt/homebrew/opt/openssl@3")
    set(OPENSSL_INCLUDE_DIR "/opt/homebrew/opt/openssl@3/include")
    set(OPENSSL_LIBRARIES "/opt/homebrew/opt/openssl@3/lib")
    set(CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} -L/opt/homebrew/opt/openssl@3/lib")
endif()

# nlohmann-json for Linux (Docker)
if(NOT APPLE)
    include_directories(/usr/include/nlohmann)
endif()

# Add cppkafka subdirectory and link
add_subdirectory(cppkafka)
include_directories(cppkafka/include)

# Find and link OpenSSL
find_package(OpenSSL REQUIRED)
include_directories(${OPENSSL_INCLUDE_DIR})

# Link libraries
target_link_libraries(MatchingEngine
    PRIVATE cppkafka
    PRIVATE OpenSSL::SSL
    PRIVATE OpenSSL::Crypto
)
