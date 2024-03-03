/*package tn.esprit.Controllers;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import tn.esprit.entities.Complaint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComplaintIndexer {
    private Directory index;
    private IndexWriter indexWriter;

    public ComplaintIndexer() throws IOException {
        index = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        indexWriter = new IndexWriter(index, config);
    }

    public void indexComplaint(Complaint complaint) throws IOException {
        Document doc = new Document();
        doc.add(new StringField("id", String.valueOf(complaint.getComplaintId()), Field.Store.YES));
        doc.add(new TextField("title", complaint.getTitle(), Field.Store.YES));
        doc.add(new TextField("description", complaint.getDescription(), Field.Store.YES));
        doc.add(new TextField("category", complaint.getCategory(), Field.Store.YES));
        doc.add(new TextField("location", complaint.getLocation(), Field.Store.YES));
        doc.add(new TextField("status", complaint.getStatus(), Field.Store.YES));
        doc.add(new TextField("datePosted", complaint.getDatePosted().toString(), Field.Store.YES));
        doc.add(new TextField("user", complaint.getUser(), Field.Store.YES));
        indexWriter.addDocument(doc);
        indexWriter.commit();
    }

    public List<Complaint> search(String queryStr) throws IOException, ParseException {
        QueryParser parser = new QueryParser("title", new StandardAnalyzer());
        Query query = parser.parse(queryStr);
        IndexSearcher searcher = new IndexSearcher(index);
        TopDocs topDocs = searcher.search(query, 10);
        List<Complaint> results = new ArrayList<>();
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            Complaint complaint = new Complaint(
                    doc.get("title"),
                    doc.get("description"),
                    doc.get("category"),
                    doc.get("location"),
                    doc.get("status"),
                    doc.get("datePosted"), // Convert to Date as needed
                    doc.get("user")
            );
            results.add(complaint);
        }
        return results;
    }
}
*/