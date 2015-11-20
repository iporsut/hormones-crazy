### How to fetch sample data
1. Create a twitter application
2. Create `resources/credentials.edn` from credential information given. See `resources/credentials.example.edn` as an example
3. In terminal, run `lein run -m hormones-crazy.sample "#HormonesTheSeries3" filename.edn`
4. Wait until you got enough data in `filename.edn`
5. Cancel running command (Ctrl-c)

### How to load sample data
```clojure
(read-string (slurp "resources/raw-sample.edn"))
```