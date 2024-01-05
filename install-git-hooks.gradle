tasks.register installGitHooks(type: Copy) {
    println '================================================'
    println 'I am installing commit-msg Git hook in .git/hooks - hang tight!'
    println '================================================'
    from new File(rootProject.rootDir, 'commit-msg')
    into { new File(rootProject.rootDir, '.git/hooks') }
}

test.dependsOn installGitHooks
