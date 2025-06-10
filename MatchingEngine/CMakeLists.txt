# cmake_minimum_required(VERSION 3.16)
# project(MatchingEngine)
# set(CMAKE_CXX_STANDARD 17)

# # Recursively grab all .cpp under src/
# file(GLOB_RECURSE SOURCES
#     CONFIGURE_DEPENDS
#     "${CMAKE_CURRENT_SOURCE_DIR}/src/*.cpp"
# )

# add_executable(MatchingEngine ${SOURCES})

# # Include headers
# include_directories(include)

# # macOS/Homebrew-specific settings
# if(APPLE)
#     include_directories(
#       /opt/homebrew/Cellar/librdkafka/2.10.0/include
#       /opt/homebrew/Cellar/librdkafka/2.10.0/include/librdkafka
#     )
#     link_directories(
#       /opt/homebrew/lib
#       /opt/homebrew/Cellar/librdkafka/2.10.0/lib
#     )
#     include_directories(/opt/homebrew/include)
#     set(OPENSSL_ROOT_DIR "/opt/homebrew/opt/openssl@3")
#     set(OPENSSL_INCLUDE_DIR "/opt/homebrew/opt/openssl@3/include")
#     set(OPENSSL_LIBRARIES "/opt/homebrew/opt/openssl@3/lib")
#     set(CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} -L/opt/homebrew/opt/openssl@3/lib")
# endif()

# # nlohmann-json for Linux (Docker)
# if(NOT APPLE)
#     include_directories(/usr/include/nlohmann)
# endif()

# # Add cppkafka subdirectory and link
# add_subdirectory(cppkafka)
# include_directories(cppkafka/include)

# # Find and link OpenSSL
# find_package(OpenSSL REQUIRED)
# include_directories(${OPENSSL_INCLUDE_DIR})

# # Link libraries
# target_link_libraries(MatchingEngine
#     PRIVATE cppkafka
#     PRIVATE OpenSSL::SSL
#     PRIVATE OpenSSL::Crypto
# )


cmake_minimum_required(VERSION 3.16)
project(MatchingEngine CXX) # Add CXX to indicate C++ project

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON) # Ensure C++17 is strictly enforced
set(CMAKE_CXX_EXTENSIONS OFF)      # Don't allow GNU extensions

# Recursively grab all .cpp under src/
file(GLOB_RECURSE SOURCES
    "${CMAKE_CURRENT_SOURCE_DIR}/src/*.cpp"
)

# Add our own include directory
include_directories(include)

# Include FetchContent module BEFORE any FetchContent_Declare/MakeAvailable calls
include(FetchContent)

# Find OpenSSL using CMake's built-in module
# This will set OPENSSL_FOUND, OPENSSL_INCLUDE_DIR, OPENSSL_LIBRARIES etc.
# And import targets like OpenSSL::SSL and OpenSSL::Crypto
find_package(OpenSSL REQUIRED)

# Find librdkafka
# On macOS, if installed via Homebrew, find_package might find it.
# Otherwise, you might need to specify hints or use a custom find module.
# For simplicity, we'll try to find it. If it fails, you might need
# to revert to manual include/link directories for librdkafka as a fallback.
find_package(RdKafka REQUIRED) # Check if RdKafka target exists


# Fetch and build cppkafka
# FetchContent will handle cloning the repo and adding it to the build.
# cppkafka's own CMakeLists.txt should define the 'cppkafka' target.
FetchContent_Declare(
    cppkafka
    GIT_REPOSITORY https://github.com/mfontanini/cppkafka.git
    GIT_TAG        master
    # Optionally specify a shallow clone for faster fetching:
    # GIT_SHALLOW    TRUE
)

# This command makes the fetched project available to your current project.
# It effectively acts like add_subdirectory for the fetched content.
FetchContent_MakeAvailable(cppkafka)

# Create the executable target
add_executable(MatchingEngine ${SOURCES})

# Link libraries to the executable
# FetchContent_MakeAvailable(cppkafka) should expose the 'cppkafka' target.
# find_package(OpenSSL) should expose OpenSSL::SSL and OpenSSL::Crypto targets.
# find_package(RdKafka) should expose the librdkafka target.
target_link_libraries(MatchingEngine
    PRIVATE cppkafka
    PRIVATE OpenSSL::SSL
    PRIVATE OpenSSL::Crypto
    PRIVATE RdKafka::RdKafka # Assuming find_package(RdKafka) created this target
)

# --- Conditional settings (fallback or specific needs) ---
# nlohmann-json for Linux (Docker) - direct include for this specific setup
if(NOT APPLE)
    include_directories(/usr/include/nlohmann)
endif()

# macOS/Homebrew-specific settings for librdkafka fallback or if find_package fails
# Only if RdKafka wasn't found by find_package or you specifically need these
if(APPLE AND NOT RdKafka_FOUND) # Only add if RdKafka wasn't found automatically
    message(STATUS "RdKafka not found via find_package, using Homebrew paths.")
    include_directories(
      /opt/homebrew/Cellar/librdkafka/2.10.0/include
      /opt/homebrew/Cellar/librdkafka/2.10.0/include/librdkafka
    )
    link_directories(
      /opt/homebrew/lib
      /opt/homebrew/Cellar/librdkafka/2.10.0/lib
    )
    # You'd then need to link to librdkafka.a or librdkafka.dylib directly
    # target_link_libraries(MatchingEngine PRIVATE librdkafka)
    # This might require more explicit linking for librdkafka if find_package fails.
    # The default RdKafka::RdKafka target from find_package is preferred.
endif()

# Important: Remove the commented out lines from your actual CMakeLists.txt
# add_subdirectory(cppkafka)
# include_directories(cppkafka/include)