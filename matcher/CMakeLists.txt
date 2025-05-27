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

# Add cppkafka subdirectory and link
add_subdirectory(cppkafka)
include_directories(cppkafka/include)

# Set OpenSSL paths for Homebrew
set(OPENSSL_ROOT_DIR "/opt/homebrew/opt/openssl@3")
set(OPENSSL_INCLUDE_DIR "/opt/homebrew/opt/openssl@3/include")
set(OPENSSL_LIBRARIES "/opt/homebrew/opt/openssl@3/lib")

# Find and link OpenSSL
find_package(OpenSSL REQUIRED)
include_directories(${OPENSSL_INCLUDE_DIR})

# Ensure Homebrew OpenSSL lib path is in linker flags for macOS
set(CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} -L/opt/homebrew/opt/openssl@3/lib")

# Link librdkafka
target_link_libraries(MatchingEngine
    PRIVATE cppkafka
    PRIVATE OpenSSL::SSL
    PRIVATE OpenSSL::Crypto
)
