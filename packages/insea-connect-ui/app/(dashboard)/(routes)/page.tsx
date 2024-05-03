import ChatAside from "../_components/chat-aside";

const MainPage = () => {
  return (
    <main className="flex flex-1 overflow-hidden">
      <section
        aria-labelledby="primary-heading"
        className="flex h-full min-w-0 flex-1 flex-col overflow-y-auto lg:order-last"
      >
        Hello from primary
      </section>

      <ChatAside />
    </main>
  );
};

export default MainPage;
