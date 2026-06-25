package com.example.dev_diaries.services;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class TaggingService {

    private final Map<Pattern, String> tagDictionary = new LinkedHashMap<>();

    public TaggingService() {

        // ==========================================
        // Data Structures & Algorithms (DSA)
        // ==========================================
        tagDictionary.put(Pattern.compile(
                "(?i)\\b(array|linked list|stack|queue|binary tree|bst|graph|trie|heap|priority queue|hash map|hash set|segment tree|fenwick tree|bit|disjoint set|dsu)\\b"),
                "Data Structures");
        tagDictionary.put(Pattern.compile(
                "(?i)\\b(sorting|merge sort|quick sort|binary search|dynamic programming|dp|knapsack|lcs|greedy|backtracking|recursion|dfs|bfs|depth first|breadth first|dijkstra|bellman ford|kruskal|prim|topological sort|kmp|two pointer|sliding window)\\b"),
                "Algorithms");

        // ==========================================
        // Competitive Programming Platforms
        // ==========================================
        tagDictionary.put(Pattern.compile(
                "(?i)\\b(leetcode|codeforces|codechef|atcoder|hackerrank|competitive programming|cp template)\\b"),
                "Competitive Programming");

        // ==========================================
        // CP Language Templates (Fast I/O & Snippets)
        // ==========================================

        // C++ CP Signatures
        tagDictionary.put(Pattern.compile(
                "(?i)(bits/stdc\\+\\+\\.h|ios_base::sync_with_stdio|cin\\.tie|cout\\.tie|#define pb|typedef long long ll)"),
                "C++ CP Template");

        // Java CP Signatures
        tagDictionary.put(Pattern.compile(
                "(?i)\\b(StringTokenizer|BufferedReader|PrintWriter|InputStreamReader|Arrays\\.sort|Collections\\.sort)\\b"),
                "Java CP Template");

        // Python CP Signatures
        tagDictionary.put(Pattern.compile(
                "(?i)\\b(sys\\.stdin\\.readline|sys\\.setrecursionlimit|collections\\.deque|heapq|defaultdict|Counter)\\b"),
                "Python CP Template");

        // ==========================================
        // Version Control & Collaboration
        // ==========================================
        tagDictionary.put(
                Pattern.compile(
                        "(?i)\\b(git|commit|push|pull|merge|rebase|checkout|clone|github|gitlab|bitbucket|svn)\\b"),
                "Git / VCS");

        // ==========================================
        // OS, Shell & Core Utilities
        // ==========================================
        tagDictionary.put(Pattern.compile(
                "(?i)\\b(sudo|apt(-get)?|yum|brew|bash|zsh|linux|ubuntu|centos|macos|windows|powershell|chmod|chown|grep|awk|sed|systemctl|ssh|scp|curl|wget)\\b"),
                "OS / Shell");

        // ==========================================
        // DevOps, CI/CD & Containers
        // ==========================================
        tagDictionary.put(
                Pattern.compile("(?i)\\b(docker|container|docker-compose|dockerfile|docker run|image|podman)\\b"),
                "Docker");
        tagDictionary.put(
                Pattern.compile("(?i)\\b(kubernetes|k8s|kubectl|minikube|pods|helm|deployment|ingress|istio)\\b"),
                "Kubernetes");
        tagDictionary.put(Pattern.compile("(?i)\\b(jenkins|github actions|gitlab ci|circleci|travis|argocd|tekton)\\b"),
                "CI/CD");
        tagDictionary.put(
                Pattern.compile("(?i)\\b(terraform|ansible|chef|puppet|pulumi|infrastructure as code|iac)\\b"),
                "Infrastructure as Code");
        tagDictionary.put(
                Pattern.compile("(?i)\\b(prometheus|grafana|elk|datadog|splunk|new relic|observability|telemetry)\\b"),
                "Monitoring");

        // ==========================================
        // Cloud Providers
        // ==========================================
        tagDictionary.put(Pattern.compile("(?i)\\b(aws|ec2|s3|lambda|iam|dynamodb|rds|vpc|cloudformation|route53)\\b"),
                "AWS");
        tagDictionary.put(Pattern.compile("(?i)\\b(azure|aks|azure functions|blob storage|cosmos db|azure ad)\\b"),
                "Azure");
        tagDictionary.put(
                Pattern.compile("(?i)\\b(gcp|google cloud|compute engine|gke|bigquery|cloud run|firebase)\\b"), "GCP");

        // ==========================================
        // Backend Languages & Frameworks
        // ==========================================
        tagDictionary.put(Pattern.compile(
                "(?i)\\b(java|spring|spring boot|jpa|hibernate|maven|gradle|mvn|pom\\.xml|quarkus|micronaut)\\b"),
                "Java / Spring");
        tagDictionary.put(
                Pattern.compile("(?i)\\b(node\\.js|node|express|npm|yarn|npx|package\\.json|nest\\.js|fastify)\\b"),
                "Node.js");
        tagDictionary.put(Pattern.compile("(?i)\\b(python|pip|django|flask|fastapi|celery|sqlalchemy)\\b"), "Python");
        tagDictionary.put(Pattern.compile("(?i)\\b(go|golang|goroutine|go mod|gin|echo)\\b"), "Go");
        tagDictionary.put(Pattern.compile("(?i)\\b(c#|\\.net|asp\\.net|entity framework|nuget|dotnet)\\b"),
                "C# / .NET");
        tagDictionary.put(Pattern.compile("(?i)\\b(c\\+\\+|cmake|gcc|clang|rust|cargo|tokio)\\b"), "C++ / Rust");
        tagDictionary.put(Pattern.compile("(?i)\\b(php|laravel|symfony|composer|wordpress)\\b"), "PHP");
        tagDictionary.put(Pattern.compile("(?i)\\b(ruby|ruby on rails|gem|bundler)\\b"), "Ruby");

        // ==========================================
        // Frontend Frameworks & Web Tech
        // ==========================================
        tagDictionary.put(
                Pattern.compile("(?i)\\b(react|reactjs|useState|useEffect|jsx|tsx|next\\.js|create-react-app|vite)\\b"),
                "React");
        tagDictionary.put(Pattern.compile("(?i)\\b(vue|vuejs|vuex|pinia|nuxt)\\b"), "Vue.js");
        tagDictionary.put(Pattern.compile("(?i)\\b(angular|rxjs|ng|zone\\.js)\\b"), "Angular");
        tagDictionary.put(Pattern.compile("(?i)\\b(svelte|sveltekit)\\b"), "Svelte");
        tagDictionary.put(Pattern.compile("(?i)\\b(html|css|tailwind|bootstrap|sass|less|styled-components)\\b"),
                "HTML / CSS");
        tagDictionary.put(
                Pattern.compile("(?i)\\b(typescript|ts|javascript|js|es6|promises|async|await|webpack|babel)\\b"),
                "JavaScript / TS");

        // ==========================================
        // Mobile Development
        // ==========================================
        tagDictionary.put(
                Pattern.compile("(?i)\\b(android|kotlin|java|android studio|apk|jetpack compose|activity|intent)\\b"),
                "Android Dev");
        tagDictionary.put(Pattern.compile("(?i)\\b(ios|swift|objective-c|xcode|cocoapods|ipa|swiftui)\\b"), "iOS Dev");
        tagDictionary.put(Pattern.compile("(?i)\\b(flutter|dart|widget|pubspec)\\b"), "Flutter");
        tagDictionary.put(Pattern.compile("(?i)\\b(react native|expo|metro bundler)\\b"), "React Native");

        // ==========================================
        // Databases (Relational & NoSQL)
        // ==========================================
        tagDictionary.put(Pattern.compile(
                "(?i)\\b(sql|postgres(ql)?|mysql|mariadb|oracle|sql server|select|insert|update|delete|join|jdbc)\\b"),
                "SQL / RDBMS");
        tagDictionary.put(Pattern.compile("(?i)\\b(mongodb|mongo|mongoose|nosql|aggregate|cassandra|couchdb)\\b"),
                "NoSQL / MongoDB");
        tagDictionary.put(Pattern.compile("(?i)\\b(neo4j|cypher|graph db)\\b"), "Graph Databases");
        tagDictionary.put(Pattern.compile("(?i)\\b(elasticsearch|solr|lucence)\\b"), "Search Engines");

        // ==========================================
        // Caching & Message Brokers (Architecture)
        // ==========================================
        tagDictionary.put(Pattern.compile("(?i)\\b(redis|memcached|hazelcast)\\b"), "Caching");
        tagDictionary.put(Pattern.compile("(?i)\\b(kafka|rabbitmq|activemq|sqs|sns|pubsub|event driven|cqrs)\\b"),
                "Message Brokers");
        tagDictionary.put(Pattern.compile("(?i)\\b(graphql|apollo|rest|grpc|protobuf|swagger|openapi)\\b"),
                "API / Protocols");

        // ==========================================
        // Testing & QA
        // ==========================================
        tagDictionary.put(Pattern.compile("(?i)\\b(jest|mocha|chai|cypress|playwright|selenium|puppeteer)\\b"),
                "JS Testing");
        tagDictionary.put(Pattern.compile("(?i)\\b(junit|testng|mockito|jacoco)\\b"), "Java Testing");
        tagDictionary.put(Pattern.compile("(?i)\\b(pytest|unittest|tdd|bdd|cucumber)\\b"), "QA / Testing");

        // ==========================================
        // Data Science & Machine Learning
        // ==========================================
        tagDictionary.put(Pattern.compile(
                "(?i)\\b(machine learning|ml|ai|deep learning|neural network|nlp|llm|openai|pytorch|tensorflow|keras|scikit-learn|pandas|numpy|jupyter)\\b"),
                "AI / Data Science");

        // ==========================================
        // Blockchain & Web3
        // ==========================================
        tagDictionary.put(Pattern.compile(
                "(?i)\\b(blockchain|web3|ethereum|smart contract|solidity|hardhat|truffle|ethers\\.js|web3\\.js|hyperledger|fabric|ipfs)\\b"),
                "Blockchain / Web3");

        // ==========================================
        // Security
        // ==========================================
        tagDictionary.put(
                Pattern.compile(
                        "(?i)\\b(oauth2?|jwt|cors|csrf|xss|sql injection|encryption|bcrypt|ssl|tls|https|owasp)\\b"),
                "Security");
    }

    public Set<String> extractTagsFromContent(String content) {
        if (content == null || content.isBlank()) {
            return Collections.emptySet();
        }

        return tagDictionary.entrySet().stream()
                .filter(entry -> entry.getKey().matcher(content).find())
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }
}